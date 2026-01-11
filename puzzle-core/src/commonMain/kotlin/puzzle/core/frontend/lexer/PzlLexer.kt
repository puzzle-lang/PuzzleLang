package puzzle.core.frontend.lexer

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.lexer.recognition.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.token.PzlToken
import puzzle.core.frontend.token.kinds.CommentKind.MultiLine
import puzzle.core.frontend.token.kinds.CommentKind.SingleLine
import puzzle.core.frontend.token.kinds.WhiteSpaceKind

private val recognitions = arrayOf(
	EOFRecognition,
	WhiteSpaceRecognition,
	TabRecognition,
	NewlineRecognition,
	CommentRecognition,
	CharRecognition,
	StringRecognition,
	NumberRecognition,
	SymbolRecognition,
	KeywordAndIdentifierRecognition,
)

class PzlLexer(
	private val input: CharArray,
	private var position: Int,
) {
	
	context(_: FileContext)
	fun nextToken(): PzlToken {
		val token = recognitions.firstNotNullOfOrNull { recognition ->
			val token = recognition.tryParse(input, position)
				?: return@firstNotNullOfOrNull null
			position = token.location.end
			when (token.kind) {
				is WhiteSpaceKind, is SingleLine, is MultiLine -> nextToken()
				else -> token
			}
		}
		return token ?: syntaxError("${input[position]} 无法被识别", position)
	}
}