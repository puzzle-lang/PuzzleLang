package puzzle.diagnostic.source

import puzzle.base.environment.PzlEnvironment
import puzzle.base.exception.PzlException
import puzzle.base.util.AnsiStyle
import puzzle.base.util.beginAnsi
import puzzle.base.util.endAnsi

object PzlExceptionPrinter {
	
	fun print(e: PzlException) {
		val message = buildString {
			if (PzlEnvironment.enableAnsiColor) {
				beginAnsi(AnsiStyle.BRIGHT_RED)
			}
			append("错误: ${e.message}")
			if (PzlEnvironment.enableErrorStack) {
				val message = e.stackTraceToString()
				val index = message.indexOf("\n")
				if (index > 0) {
					append(message.substring(index))
				}
			}
			if (PzlEnvironment.enableAnsiColor) {
				endAnsi()
			}
			appendLine()
		}
		print(message)
	}
}