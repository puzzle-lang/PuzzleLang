package puzzle.core.frontend

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import puzzle.core.frontend.ast.AstDebugWriter
import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.builtin.BuiltinAstGenerator
import puzzle.core.frontend.discovery.ProjectSourceCollector
import puzzle.core.frontend.lexer.FileLexerScanner
import puzzle.core.frontend.model.AstModule
import puzzle.core.frontend.model.AstProject
import puzzle.core.frontend.model.AstRoot
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlParser
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.semantics.PzlSemantics
import puzzle.core.util.PathWrapper
import puzzle.core.util.format
import kotlin.time.Duration
import kotlin.time.TimeSource.Monotonic.markNow
import kotlin.time.measureTimedValue

suspend fun processFrontend(projectPath: PathWrapper) = coroutineScope {
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
					val files = jobs.awaitAll()
					AstModule(
						name = module.name,
						path = module.path,
						builtin = false,
						files = files
					)
				}
			}
			val modules = jobs.awaitAll()
			AstProject(
				name = project.name,
				path = project.path,
				builtin = false,
				modules = modules
			)
		}
	}
	val projects = jobs.awaitAll()
	val builtinProject = BuiltinAstGenerator.generate()
	val root = AstRoot(projects + builtinProject)
	PzlSemantics.analyze(root)
	AstDebugWriter.write(projectPath, root)
}

private fun processFile(path: PathWrapper, maxPathLength: Int): AstFile {
	val markStart = markNow()
	val source = measureTimedValue { path.readText().toCharArray() }
	val lineStarts = source.value.getLineStarts()
	val context = PzlContext(path, lineStarts)
	return context(context) {
		val tokens = measureTimedValue { FileLexerScanner.scan(source.value) }
		val cursor = PzlTokenCursor(tokens.value)
		val node = measureTimedValue { PzlParser.parse(cursor) }
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
		)
		node.value
	}
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
		append("[TOTAL] $totalTime  ")
		append("[READ] $charSize  $readTime  ")
		append("[LEXER] $tokenSize  $lexerTime  $lexerSpeed  ")
		append("[PARSER] $parserTime  $parserSpeed\n")
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