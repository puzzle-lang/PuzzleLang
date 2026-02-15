package puzzle.diagnostic.source

import puzzle.core.environment.PzlEnvironment
import puzzle.core.exception.PzlException
import puzzle.core.util.AnsiStyle
import puzzle.core.util.beginAnsi
import puzzle.core.util.endAnsi

object PzlExceptionPrinter {
	
	fun print(e: PzlException) {
		val message = buildString {
			if (PzlEnvironment.enableAnsiColor) {
				beginAnsi(AnsiStyle.BRIGHT_RED)
			}
			append("错误: ${e.message}")
			if (PzlEnvironment.enableStackTrace) {
				val message = e.stackTraceToString()
				val index = message.indexOf("\n")
				if (index > 0) {
//					append(message.substring(index))
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