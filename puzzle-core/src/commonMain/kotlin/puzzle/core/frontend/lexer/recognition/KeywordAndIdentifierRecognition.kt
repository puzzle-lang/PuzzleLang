package puzzle.core.frontend.lexer.recognition

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.token.PzlToken
import puzzle.core.frontend.token.kinds.IdentifierKind
import puzzle.core.frontend.token.kinds.KeywordKind
import puzzle.core.util.isIdentifierPart
import puzzle.core.util.isIdentifierStart

object KeywordAndIdentifierRecognition : TokenRecognition {
	
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
			syntaxError("不合法的标识符", start)
		}
		val keywordKind = KeywordKind.kinds.find { it.value == identifier }
		if (keywordKind != null) {
			return PzlToken(keywordKind, start span position)
		}
		val kind = IdentifierKind(identifier)
		return PzlToken(kind, start span position)
	}
}