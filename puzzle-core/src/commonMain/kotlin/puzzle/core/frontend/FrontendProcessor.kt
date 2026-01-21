package puzzle.core.frontend

import kotlinx.coroutines.*
import puzzle.core.cli.option
import puzzle.core.frontend.ast.AstDebugWriter
import puzzle.core.frontend.ast.builtin.BuiltinAstGenerator
import puzzle.core.frontend.discovery.ProjectSourceCollector
import puzzle.core.frontend.lexer.FileLexerScanner
import puzzle.core.frontend.model.Dependence
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.RootContext
import puzzle.core.frontend.parser.PzlParser
import puzzle.core.frontend.semantics.PzlSymbolBuilder
import puzzle.core.util.*
import kotlin.time.Duration
import kotlin.time.TimeSource.Monotonic.markNow
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

suspend fun processFrontend(path: String) = coroutineScope {
	val projectPath = path(path)
	val collectDuration = measureTime { ProjectSourceCollector.collect(projectPath) }
	if (option.info.enableProgress) {
		println("项目源收集用时${CHINESE_SPACE.repeat(4)}: ${collectDuration.format()}")
	}
	val compileDuration = measureTime {
		compileAllFiles()
	}
	if (option.info.enableProgress) {
		println("项目源代码分析用时${CHINESE_SPACE.repeat(2)}: ${compileDuration.format()}")
	}
	
	val builtinProject = measureTimedValue { BuiltinAstGenerator.generate() }
	if (option.info.enableProgress) {
		println("内建类型生成用时${CHINESE_SPACE.repeat(3)}: ${builtinProject.duration.format()}")
	}
	RootContext.projects += builtinProject.value
	
	fillInDefaultDependencies()
	
	val rootSymbol = measureTimedValue { PzlSymbolBuilder.buildRootSymbol() }
	if (option.info.enableProgress) {
		println("全局符号表创建用时${CHINESE_SPACE.repeat(2)}: ${rootSymbol.duration.format()}")
	}
	
	if (option.debugFeature.enableOutputAstJson) {
		val writeDuration = measureTime {
			AstDebugWriter.write(projectPath)
		}
		if (option.info.enableProgress) {
			println("抽象语法树导出用时${CHINESE_SPACE.repeat(2)}: ${writeDuration.format()}")
		}
	}
}

private suspend fun CoroutineScope.compileAllFiles() {
	RootContext.projects.map { project ->
		async(Dispatchers.Default) {
			project.modules.map { module ->
				launch(Dispatchers.Default) {
					module.files.map { file ->
						launch(Dispatchers.Default) {
							context(file) {
								compileFile()
							}
						}
					}.joinAll()
				}
			}.joinAll()
		}
	}.joinAll()
}

context(file: FileContext)
private fun compileFile() {
	val markStart = markNow()
	val source = measureTimedValue { file.path.readText().toCharArray() }
	file.lineStarts = source.value.getLineStarts()
	val tokens = measureTimedValue { FileLexerScanner.scan(source.value) }
	file.tokens = tokens.value
	val node = measureTimedValue { PzlParser.parse() }
	file.node = node.value
	val symbol = measureTimedValue { PzlSymbolBuilder.buildFileSymbol() }
	file.symbol = symbol.value
	if (option.info.enableFile) {
		val totalDuration = markStart.elapsedNow()
		printDurations(
			path = file.path,
			charSize = source.value.size,
			tokenSize = tokens.value.size,
			totalDuration = totalDuration,
			readDuration = source.duration,
			lexerDuration = tokens.duration,
			parserDuration = node.duration,
			scopeDuration = symbol.duration
		)
	}
}

context(_: FileContext)
private fun printDurations(
	path: PathWrapper,
	charSize: Int,
	tokenSize: Int,
	totalDuration: Duration,
	readDuration: Duration,
	lexerDuration: Duration,
	parserDuration: Duration,
	scopeDuration: Duration,
) {
	val message = buildString {
		val path = path.absolutePath
		append(path.toAnsiString(AnsiStyle.BRIGHT_BLUE, AnsiStyle.UNDERLINE))
		appendAnsi(AnsiStyle.UNDERLINE_OFF)
		val maxPathLength = RootContext.maxPathLength
		append(" ${"-".repeat(maxPathLength - path.length + 2)}> ".toAnsiString(AnsiStyle.BRIGHT_CYAN))
		val totalTime = totalDuration.format()
		val readTime = readDuration.format()
		val lexerTime = lexerDuration.format()
		val parserTime = parserDuration.format()
		val lexerSpeed = (charSize * 1_000_000L / lexerDuration.inWholeNanoseconds).toString().padStart(5) + " chars/ms"
		val parserSpeed = (tokenSize * 1_000_000L / parserDuration.inWholeNanoseconds).toString().padStart(5) + " tokens/ms"
		val charSize = charSize.toString().padStart(6)
		val tokenSize = tokenSize.toString().padStart(6)
		val scopeDuration = scopeDuration.format()
		append("[总计] ".toAnsiString(AnsiStyle.BRIGHT_GREEN))
		append("$totalTime  ".toAnsiString(AnsiStyle.BRIGHT_WHITE))
		append("[读取] ".toAnsiString(AnsiStyle.BRIGHT_GREEN))
		append("$charSize $readTime  ".toAnsiString(AnsiStyle.BRIGHT_WHITE))
		append("[词法] ".toAnsiString(AnsiStyle.BRIGHT_GREEN))
		append("$tokenSize $lexerTime $lexerSpeed  ".toAnsiString(AnsiStyle.BRIGHT_WHITE))
		append("[语法] ".toAnsiString(AnsiStyle.BRIGHT_GREEN))
		append("$parserTime $parserSpeed  ".toAnsiString(AnsiStyle.BRIGHT_WHITE))
		append("[语义]".toAnsiString(AnsiStyle.BRIGHT_GREEN))
		append(scopeDuration.toAnsiString(AnsiStyle.BRIGHT_WHITE))
		endAnsi()
		appendLine()
	}
	print(message)
}

private fun CharArray.getLineStarts(): IntArray {
	val starts = mutableListOf(0)
	this.forEachIndexed { index, char ->
		if (char == '\n' && index + 1 < this.size) {
			starts += index + 1
		}
	}
	return starts.toIntArray()
}

private fun fillInDefaultDependencies() {
	RootContext.projects.forEach { project ->
		project.modules.forEach { module ->
			when (project.name) {
				"puzzle-builtin" if module.name == "puzzle-core" -> return@forEach
				"puzzle-source" if module.name == "puzzle-core" -> {
					module.deps += setOf(Dependence("puzzle-builtin", "puzzle-core"))
				}
				
				else -> {
					module.deps += setOf(
						Dependence("puzzle-builtin", "puzzle-core"),
						Dependence("puzzle-source", "puzzle-core")
					)
				}
			}
		}
	}
}