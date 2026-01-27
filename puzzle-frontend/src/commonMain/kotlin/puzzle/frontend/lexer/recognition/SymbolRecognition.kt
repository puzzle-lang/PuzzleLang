package puzzle.frontend.lexer.recognition

import puzzle.core.util.safeString
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.token.PzlToken
import puzzle.token.kinds.OperatorKind.IN
import puzzle.token.kinds.OperatorKind.NOT_IN
import puzzle.token.kinds.SymbolKind

internal object SymbolRecognition : TokenRecognition {
	
	private val symbols = SymbolKind.kinds
		.groupBy { it.value.length }
		.mapValues { (_, value) -> value.associateBy { it.value } }
	
	private val maxLength = SymbolKind.kinds.maxOf { it.value.length }
	
	private val starts = SymbolKind.kinds.map { it.value.first() }.toSet()
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] !in starts) return null
		for (length in maxLength downTo 1) {
			val symbol = input.safeString(start, length) ?: continue
			val kind = symbols[length]!![symbol] ?: continue
			if (symbol == IN.value || symbol == NOT_IN.value) {
				val next = input[start + length]
				if (next != ' ' && next != '\t') return null
			}
			return PzlToken(kind, start span start + length)
		}
		return null
	}
}