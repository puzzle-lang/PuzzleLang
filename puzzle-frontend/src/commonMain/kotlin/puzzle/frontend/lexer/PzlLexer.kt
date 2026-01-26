package puzzle.frontend.lexer

import puzzle.context.FileContext
import puzzle.frontend.lexer.recognition.*
import puzzle.token.PzlToken
import puzzle.token.kinds.CommentKind.MultiLine
import puzzle.token.kinds.CommentKind.SingleLine
import puzzle.token.kinds.WhiteSpaceKind

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
		return token ?: lexicalError("${input[position]} 无法被识别", position)
	}
}