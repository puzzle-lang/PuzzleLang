package puzzle.frontend.lexer.recognition

import puzzle.base.location.span
import puzzle.context.FileContext
import puzzle.frontend.lexer.lexicalError
import puzzle.frontend.util.isIdentifierPart
import puzzle.frontend.util.isIdentifierStart
import puzzle.token.PzlToken
import puzzle.token.kinds.IdentifierKind
import puzzle.token.kinds.KeywordKind

internal object KeywordAndIdentifierRecognition : TokenRecognition {
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (!input[start].isIdentifierStart()) return null
		var position = start + 1
		while (position < input.size && input[position].isIdentifierPart()) {
			position++
		}
		val identifier = input.concatToString(start, position)
		val isValid = identifier == "_" || identifier.any { it != '_' }
		if (!isValid) {
			lexicalError("不合法的标识符", start)
		}
		val keywordKind = KeywordKind.kinds.find { it.value == identifier }
		if (keywordKind != null) {
			return PzlToken(keywordKind, start span position)
		}
		val kind = IdentifierKind(identifier)
		return PzlToken(kind, start span position)
	}
}