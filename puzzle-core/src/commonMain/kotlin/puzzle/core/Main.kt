package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import puzzle.core.cli.*
import puzzle.core.exception.PzlException
import puzzle.core.exception.cliError
import puzzle.core.frontend.model.RootContext
import puzzle.core.frontend.processFrontend
import puzzle.core.util.*
import kotlin.time.measureTime

fun main(vararg args: String) {
	try {
		runBlocking(Dispatchers.Default) {
			val mode = args.firstOrNull()
				?: return@runBlocking PzlCliMessage.help()
			when (mode) {
				"build" -> build(args.drop(1))
				"help" -> PzlCliMessage.help()
				"version" -> PzlCliMessage.version()
				else -> PzlCliMessage.unknown()
			}
		}
	} catch (e: PzlException) {
		withAnsiStyle(AnsiStyle.BRIGHT_RED) {
			println("e: ${e.message}")
		}
//		throw e
	} catch (e: Exception) {
		throw e
	}
}

private suspend fun build(args: List<String>) {
	val root = RootContext()
	val options = parseCliOptions(args)
	root.options = options
	val pathOption = options.findOption<PathOption>() ?: cliError("缺少 --path 选项")
	context(root) {
		val duration = measureTime { processFrontend(pathOption) }
		whenEnableInfoProgress {
			println("执行用时${CHINESE_SPACE.repeat(7)}: ${duration.format()}")
			val usage = getCurrentMemoryUsage()
			println("内存使用 ${"[$usage]".padStart(22)}")
		}
	}
}