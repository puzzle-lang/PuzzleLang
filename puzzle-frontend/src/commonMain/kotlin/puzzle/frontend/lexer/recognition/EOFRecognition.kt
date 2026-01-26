package puzzle.frontend.lexer.recognition

import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.token.PzlToken
import puzzle.token.kinds.MetaKind.EOF

internal object EOFRecognition : TokenRecognition {
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (start < input.size) return null
		return PzlToken(EOF, start span start + 1)
	}
}