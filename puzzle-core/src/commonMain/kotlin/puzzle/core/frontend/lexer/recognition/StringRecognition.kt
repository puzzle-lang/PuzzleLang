package puzzle.core.frontend.lexer.recognition

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.lexer.TemplateExpressionLexerScanner
import puzzle.core.frontend.lexer.recognition.StringRecognition.CharType.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.token.PzlToken
import puzzle.core.frontend.token.kinds.MetaKind
import puzzle.core.frontend.token.kinds.StringKind
import puzzle.core.frontend.token.kinds.StringKind.Template.Part
import puzzle.core.util.*

object StringRecognition : TokenRecognition {
	
	private val standardEscapeCodes = setOf('\'', '"', '\\', 'n', 'r', 't', 'b', 'f')
	
	private const val UNICODE_CODE = 'u'
	
	private const val TRIPLE_QUOTATION = """""""""
	
	private const val QUOTATION = '\"'
	
	private const val DOLLAR = '$'
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val dollarCount = when (input[start]) {
			QUOTATION -> 0
			DOLLAR -> {
				var count = 1
				while (input[start + count] == DOLLAR) {
					count++
				}
				if (input[start + count] == QUOTATION) count else return null
			}
			
			else -> return null
		}
		val isMultiLine = input.safeString(start + dollarCount, 3) == TRIPLE_QUOTATION
		return if (dollarCount == 0) {
			parseText(input, start, isMultiLine)
		} else {
			parseTemplate(input, start, isMultiLine, dollarCount)
		}
	}
	
	context(_: FileContext)
	private fun parseText(input: CharArray, start: Int, isMultiLine: Boolean): PzlToken {
		val offset = if (isMultiLine) 3 else 1
		val textStart = start + offset
		var position = textStart
		while (position < input.size) {
			position += when (parseCharType(input, position, isMultiLine)) {
				NORMAL -> 1
				ESCAPE -> 2
				UNICODE -> 6
				NEWLINE -> if (isMultiLine) 1 else syntaxError("请使用多行字符串", position)
				else -> break
			}
		}
		val raw = input.concatToString(textStart, position)
		val kind = StringKind.Text(if (isMultiLine) raw.trimIndent() else raw)
		return PzlToken(kind, start span position + offset)
	}
	
	context(file: FileContext)
	private fun parseTemplate(input: CharArray, start: Int, isMultiLine: Boolean, dollarCount: Int): PzlToken {
		val offset = if (isMultiLine) 3 else 1
		var textStart = start + dollarCount + offset
		var position = textStart
		val rawParts = mutableListOf<Part>()
		while (position < input.size) {
			val type = parseCharType(input, position, isMultiLine, dollarCount)
			position += when (type) {
				NORMAL -> 1
				ESCAPE -> 2
				UNICODE -> 6
				END -> {
					if (textStart < position) {
						val value = input.concatToString(textStart, position)
						rawParts += Part.Text(value, textStart span position)
					}
					break
				}
				
				NEWLINE -> if (isMultiLine) 1 else syntaxError("请使用多行字符串模版", position)
				
				else -> {
					if (textStart < position) {
						val value = input.concatToString(textStart, position)
						rawParts += Part.Text(value, textStart span position)
					}
					val start = position
					val tokens = when (type) {
						SIMPLE_INTERPOLATION -> {
							position += dollarCount
							val token = KeywordAndIdentifierRecognition.tryParse(input, position)!!
							position = token.location.end
							listOf(token, PzlToken(MetaKind.EOF, token.location.end span token.location.end + 1))
						}
						
						EXPRESSION_INTERPOLATION -> {
							position += dollarCount + 1
							val tokens = TemplateExpressionLexerScanner.scan(input, position)
							if (tokens.isEmpty()) {
								syntaxError("字符串插值缺少表达式", position)
							}
							position = tokens.last().location.end
							tokens
						}
					}
					rawParts += Part.Expression(tokens, start span position)
					textStart = position
					continue
				}
			}
		}
		val parts = if (isMultiLine) rawParts.trimIndent() else rawParts
		val kind = StringKind.Template(parts)
		return PzlToken(kind, start span position + offset)
	}
	
	private fun MutableList<Part>.trimIndent(): List<Part> {
		if (this.isEmpty()) return emptyList()
		val first = this.first()
		if (first is Part.Text) {
			val value = first.value.trimStart(' ', '\t')
			if (value.first() == '\n') {
				this[0] = Part.Text(value.removePrefix("\n"), first.location)
			}
		}
		val last = this.last()
		if (last is Part.Text) {
			val value = last.value.trimEnd(' ', '\t')
			if (value.last() == '\n') {
				this[this.lastIndex] = Part.Text(value.removeSuffix("\n"), last.location)
			}
		}
		var indentWidth = Int.MAX_VALUE
		val formattedIndices = mutableListOf<Int>()
		this.forEachIndexed { index, part ->
			if (part !is Part.Text || '\n' !in part.value) return@forEachIndexed
			val current = part.value.getIndentWidth(ignoreFirstLine = index > 0)!!
			formattedIndices += index
			if (current < indentWidth) {
				indentWidth = current
			}
		}
		if (indentWidth == Int.MAX_VALUE) return this
		formattedIndices.forEach { index ->
			val part = this[index] as Part.Text
			val value = part.value.removeIndent(indentWidth, ignoreFirstLine = index > 0)
			this[index] = Part.Text(value, part.location)
		}
		return this
	}
	
	context(_: FileContext)
	
	private fun parseCharType(input: CharArray, position: Int, isMultiLine: Boolean, dollarCount: Int = 0): CharType {
		return when (input[position]) {
			'\\' -> {
				if (position + 1 >= input.size) {
					syntaxError("语法错误", position)
				}
				when (val escape = input[position + 1]) {
					in standardEscapeCodes -> ESCAPE
					
					UNICODE_CODE -> {
						val unicode = input.safeString(position + 2, 4)
							?: syntaxError("语法错误", position)
						if (!unicode.isHex()) {
							syntaxError("非法转译字符: '${input.safeString(position, 6)}'", position)
						}
						UNICODE
					}
					
					else -> syntaxError("非法转译字符: '\\$escape'", position)
				}
			}
			
			'\n' -> NEWLINE
			
			QUOTATION if (!isMultiLine || (input.safeString(position, 3) == TRIPLE_QUOTATION && input.getOrNull(position + 3) != QUOTATION)) -> END
			DOLLAR if dollarCount > 0 -> {
				val dollars = input.safeString(position, dollarCount) ?: return NORMAL
				if (dollars.all { it == DOLLAR }) {
					val char = input.getOrNull(position + dollarCount) ?: return NORMAL
					when {
						char == '{' -> EXPRESSION_INTERPOLATION
						char.isIdentifierStart() -> SIMPLE_INTERPOLATION
						else -> NORMAL
					}
				} else NORMAL
			}
			
			else -> NORMAL
		}
	}
	
	enum class CharType {
		NORMAL,
		ESCAPE,
		UNICODE,
		SIMPLE_INTERPOLATION,
		EXPRESSION_INTERPOLATION,
		NEWLINE,
		END
	}
}