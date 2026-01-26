package puzzle.frontend.lexer.recognition

import puzzle.base.location.span
import puzzle.context.FileContext
import puzzle.frontend.lexer.lexicalError
import puzzle.token.PzlToken
import puzzle.token.kinds.CommentKind.*

internal object CommentRecognition : TokenRecognition {
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (start + 1 >= input.size || input[start] != '/') return null
		return when (input[start + 1]) {
			'/' -> parseSingleLineComment(input, start)
			'*' -> if (input.getOrNull(start + 2) == '*') parseDocComment(input, start) else parseMultiLineComment(input, start)
			else -> null
		}
	}
	
	private fun parseSingleLineComment(input: CharArray, start: Int): PzlToken {
		var position = start + 2
		while (position < input.size && input[position] != '\n') {
			position++
		}
		val comment = input.concatToString(start + 2, position).trim()
		val kind = SingleLine(comment)
		return PzlToken(kind, start span position)
	}
	
	context(_: FileContext)
	private fun parseDocComment(input: CharArray, start: Int): PzlToken {
		var position = start + 3
		var isEnd = false
		while (position + 1 < input.size) {
			when (input[position]) {
				'*' if input[position + 1] == '/' -> {
					isEnd = true
					break
				}
				
				else -> position++
			}
		}
		if (!isEnd) {
			lexicalError("文档注释未结束", start)
		}
		val comment = input.concatToString(start + 3, position).split("\n")
			.joinToString("\n") { it.trim().trimStart('*').trimStart() }
			.trim('\n')
		val kind = Doc(comment)
		return PzlToken(kind, start span position + 2)
	}
	
	context(_: FileContext)
	private fun parseMultiLineComment(input: CharArray, start: Int): PzlToken {
		var position = start + 2
		var isEnd = false
		while (position + 1 < input.size) {
			when (input[position]) {
				'*' if input[position + 1] == '/' -> {
					isEnd = true
					break
				}
				
				else -> position++
			}
		}
		if (!isEnd) {
			lexicalError("多行注释未结束", start)
		}
		val comment = input.concatToString(start + 2, position).split("\n")
			.joinToString("\n") { it.trim() }
			.trim('\n')
		val kind = MultiLine(comment)
		return PzlToken(kind, start span position + 2)
	}
}