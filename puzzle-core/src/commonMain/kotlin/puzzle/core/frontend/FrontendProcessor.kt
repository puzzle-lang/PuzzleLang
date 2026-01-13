package puzzle.core.frontend

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import puzzle.core.cli.PathOption
import puzzle.core.frontend.ast.AstDebugWriter
import puzzle.core.frontend.ast.builtin.BuiltinAstGenerator
import puzzle.core.frontend.discovery.ProjectSourceCollector
import puzzle.core.frontend.lexer.FileLexerScanner
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.model.ProjectContext
import puzzle.core.frontend.model.RootContext
import puzzle.core.frontend.parser.PzlParser
import puzzle.core.frontend.semantics.PzlSymbolBuilder
import puzzle.core.util.PathWrapper
import puzzle.core.util.format
import puzzle.core.util.path
import kotlin.time.Duration
import kotlin.time.TimeSource.Monotonic.markNow
import kotlin.time.measureTimedValue

suspend fun processFrontend(pathOption: PathOption) = coroutineScope {
	val projectPath = path(pathOption.path)
	val rootSource = measureTimedValue { ProjectSourceCollector.collect(projectPath) }
	println("项目源收集用时: ${rootSource.duration.format()}")
	val maxPathLength = rootSource.value.maxPathLength
	val jobs = rootSource.value.projectSources.map { project ->
		async(Dispatchers.Default) {
			val jobs = project.moduleSources.map { module ->
				async(Dispatchers.Default) {
					val jobs = module.sourcePaths.map { path ->
						async(Dispatchers.Default) {
							processFile(path, maxPathLength)
						}
					}
					ModuleContext(
						name = module.name,
						path = module.path,
						builtin = false,
						files = jobs.awaitAll()
					)
				}
			}
			ProjectContext(
				name = project.name,
				path = project.path,
				builtin = false,
				modules = jobs.awaitAll()
			)
		}
	}
	val projects = jobs.awaitAll()
	val builtinProject = BuiltinAstGenerator.generate()
	val root = RootContext(projects + builtinProject)
	context(root) {
		val rootSymbol = measureTimedValue { PzlSymbolBuilder.buildRootSymbol() }
		println("程序符号表创建用时: ${rootSymbol.duration.format()}")
	}
	AstDebugWriter.write(projectPath, root)
}

private fun processFile(path: PathWrapper, maxPathLength: Int): FileContext {
	val context = FileContext(false)
	context(context) {
		val markStart = markNow()
		context.sourcePath = path
		val source = measureTimedValue { path.readText().toCharArray() }
		context.lineStarts = source.value.getLineStarts()
		val tokens = measureTimedValue { FileLexerScanner.scan(source.value) }
		context.tokens = tokens.value
		val node = measureTimedValue { PzlParser.parse() }
		context.node = node.value
		val symbol = measureTimedValue { PzlSymbolBuilder.buildFileSymbol() }
		context.symbol = symbol.value
		val totalDuration = markStart.elapsedNow()
		printDurations(
			path = path,
			maxPathLength = maxPathLength,
			charSize = source.value.size,
			tokenSize = tokens.value.size,
			totalDuration = totalDuration,
			readDuration = source.duration,
			lexerDuration = tokens.duration,
			parserDuration = node.duration,
			scopeDuration = symbol.duration
		)
	}
	return context
}

private fun printDurations(
	path: PathWrapper,
	maxPathLength: Int,
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
		append(path)
		append(" ${"-".repeat(maxPathLength - path.length)}--> ")
		val totalTime = totalDuration.format().padStart(9, ' ')
		val readTime = readDuration.format().padStart(9, ' ')
		val lexerTime = lexerDuration.format().padStart(9, ' ')
		val parserTime = parserDuration.format().padStart(9, ' ')
		val lexerSpeed = (charSize * 1_000_000L / lexerDuration.inWholeNanoseconds).toString().padStart(5, ' ') + " chars/ms"
		val parserSpeed = (tokenSize * 1_000_000L / parserDuration.inWholeNanoseconds).toString().padStart(5, ' ') + " tokens/ms"
		val charSize = charSize.toString().padStart(6, ' ')
		val tokenSize = tokenSize.toString().padStart(6, ' ')
		val scopeDuration = scopeDuration.format().padStart(9, ' ')
		append("[用时] $totalTime  ")
		append("[读取] $charSize  $readTime  ")
		append("[词法] $tokenSize  $lexerTime  $lexerSpeed  ")
		append("[语法] $parserTime  $parserSpeed  ")
		append("[语义] $scopeDuration\n")
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