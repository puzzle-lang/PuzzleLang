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
		printPzlException(e)
	} catch (e: Throwable) {
		throw e
	}
}

private suspend fun build(args: List<String>) {
	val options = parseCliOptions(args)
	RootContext.options = options
	val pathOption = options.findOption<PathOption>() ?: cliError("缺少 --path 选项")
	val duration = measureTime { processFrontend(pathOption) }
	if (info.enableProgress) {
		println("执行用时${CHINESE_SPACE.repeat(7)}: ${duration.format()}")
		val usage = getCurrentMemoryUsage()
		println("内存使用 ${"[$usage]".padStart(22)}")
	}
}

private fun printPzlException(e: Throwable) {
	val message = buildString {
		beginAnsi(AnsiStyle.RED)
		append("错误: ")
		if (debugFeature.enableErrorStack) {
			appendLine(e.message)
			val message = e.stackTraceToString()
			val index = message.indexOf("\n")
			if (index == -1) {
				append(message)
			} else {
				append(message.substring(index + 1))
			}
		} else {
			append(e.message)
		}
		endAnsi()
		appendLine()
	}
	print(message)
}