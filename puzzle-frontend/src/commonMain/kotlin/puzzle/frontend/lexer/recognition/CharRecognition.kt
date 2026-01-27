package puzzle.frontend.lexer.recognition

import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.core.util.isHex
import puzzle.core.util.safeString
import puzzle.frontend.lexer.lexicalError
import puzzle.token.PzlToken
import puzzle.token.kinds.CharKind

internal object CharRecognition : TokenRecognition {
	
	private val standardEscapeCodes = setOf('\'', '"', '\\', 'n', 'r', 't', 'b', 'f')
	
	private const val UNICODE_CODE = 'u'
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input.getOrNull(start) != '\'') return null
		if (input.getOrNull(start + 2) == '\'' && input[start + 1] != '\\') {
			val value = input[start + 1].toString()
			return PzlToken(CharKind(value), start span start + 3)
		}
		if (input.getOrNull(start + 3) == '\'' && input[start + 1] == '\\') {
			val escape = input[start + 2]
			if (escape in standardEscapeCodes) {
				val kind = CharKind("\\$escape")
				return PzlToken(kind, start span start + 4)
			} else {
				lexicalError("非法转义字符: '\\$escape'", start + 1)
			}
		}
		if (input.getOrNull(start + 7) == '\'' && input[start + 1] == '\\' && input[start + 2] == UNICODE_CODE) {
			val unicode = input.safeString(start + 3, 4) ?: lexicalError("语法错误", start)
			if (!unicode.isHex()) {
				lexicalError("非法转义字符: '\\u$unicode'", start + 1)
			}
			val kind = CharKind("\\u$unicode")
			return PzlToken(kind, start span start + 8)
		}
		lexicalError("字符语法错误", start + 1)
	}
}