package puzzle.frontend.lexer.recognition

import puzzle.base.location.span
import puzzle.base.util.isBinary
import puzzle.base.util.isDecimal
import puzzle.base.util.isHex
import puzzle.context.FileContext
import puzzle.frontend.lexer.lexicalError
import puzzle.token.PzlToken
import puzzle.token.kinds.NumberKind
import puzzle.token.kinds.NumberSystem
import puzzle.token.kinds.NumericType

internal object NumberRecognition : TokenRecognition {
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val system = getNumberSystemOrNull(input, start) ?: return null
		return when (system) {
			NumberSystem.BINARY -> parseBinaryPzlToken(input, start)
			NumberSystem.DECIMAL -> parseDecimalPzlToken(input, start)
			NumberSystem.HEX -> parseHexPzlToken(input, start)
		}
	}
	
	context(_: FileContext)
	private fun getNumberSystemOrNull(input: CharArray, start: Int): NumberSystem? {
		val first = input[start]
		if (first != '.' && !first.isDecimal()) return null
		val second = input.getOrNull(start + 1)
			?: return if (first.isDecimal()) NumberSystem.DECIMAL else null
		return when (first) {
			'.' -> if (second.isDecimal()) NumberSystem.DECIMAL else null
			'0' -> when (second) {
				'b', 'B' -> NumberSystem.BINARY
				'x', 'X' -> NumberSystem.HEX
				else -> NumberSystem.DECIMAL
			}
			
			else -> NumberSystem.DECIMAL
		}
	}
	
	private const val BINARY_UINT_MAX_LENGTH = 32
	private const val BINARY_ULONG_MAX_LENGTH = 64
	
	context(_: FileContext)
	private fun parseBinaryPzlToken(input: CharArray, start: Int): PzlToken {
		var position = start + 2
		var is8Byte = false
		var isUnsigned = false
		val binary = buildString {
			while (true) {
				val char = input.getOrNull(position) ?: break
				when {
					char.isBinary() -> {
						this.append(char)
						position++
					}
					
					char == 'U' || char == 'u' -> {
						isUnsigned = true
						position++
						val nextChar = input.getOrNull(position) ?: break
						if (nextChar == 'L') {
							is8Byte = true
							position++
						}
						break
					}
					
					char == 'L' -> {
						is8Byte = true
						position++
						break
					}
					
					char == 'F' || char == 'f' -> numberFormatError(position)
					
					else -> break
				}
			}
		}
		if (binary.isEmpty()) {
			numberFormatError(position)
		}
		if (!is8Byte && (isUnsigned && binary.length > BINARY_UINT_MAX_LENGTH) || (!isUnsigned && binary.length >= BINARY_UINT_MAX_LENGTH)) {
			is8Byte = true
		}
		if ((isUnsigned && binary.length > BINARY_ULONG_MAX_LENGTH) || (!isUnsigned && binary.length >= BINARY_ULONG_MAX_LENGTH)) {
			numberValueIsOutOfRangeError(start)
		}
		val type = NumericType.of(isFloatingPoint = false, isUnsigned = isUnsigned, is8Byte = is8Byte)
		val kind = NumberKind(binary, NumberSystem.BINARY, type)
		return PzlToken(kind, start span position)
	}
	
	private const val DECIMAL_INT_MAX = Int.MAX_VALUE.toString()
	private val DECIMAL_UINT_MAX = UInt.MAX_VALUE.toString()
	private const val DECIMAL_LONG_MAX = Long.MAX_VALUE.toString()
	private val DECIMAL_ULONG_MAX = ULong.MAX_VALUE.toString()
	
	context(_: FileContext)
	private fun parseDecimalPzlToken(input: CharArray, start: Int): PzlToken {
		var position = start
		var isDecimal = false
		var is8Byte = false
		var isUnsigned = false
		val decimal = buildString {
			while (true) {
				val char = input.getOrNull(position) ?: break
				when {
					char.isDecimal() -> {
						this.append(char)
						position++
					}
					
					char == '.' -> {
						if (isDecimal) {
							break
						}
						val nextChar = input.getOrNull(position + 1) ?: break
						if (!nextChar.isDecimal()) break
						this.append('.')
						this.append(nextChar)
						isDecimal = true
						is8Byte = true
						position += 2
					}
					
					char == 'F' || char == 'f' -> {
						isDecimal = true
						position++
						is8Byte = false
						break
					}
					
					char == 'U' || char == 'u' -> {
						isUnsigned = true
						position++
						val nextChar = input.getOrNull(position) ?: break
						if (nextChar == 'L') {
							position++
							is8Byte = true
						}
						break
					}
					
					char == 'L' -> {
						position++
						is8Byte = true
						break
					}
					
					else -> break
				}
			}
		}
		val type = if (isDecimal) {
			NumericType.of(isFloatingPoint = true, isUnsigned = isUnsigned, is8Byte = is8Byte)
		} else {
			if (!is8Byte) {
				is8Byte = when {
					isUnsigned && (decimal.length > DECIMAL_UINT_MAX.length || (decimal.length == DECIMAL_UINT_MAX.length && decimal > DECIMAL_UINT_MAX)) -> true
					!isUnsigned && (decimal.length > DECIMAL_INT_MAX.length || (decimal.length == DECIMAL_INT_MAX.length && decimal > DECIMAL_INT_MAX)) -> true
					else -> false
				}
			}
			when {
				!is8Byte -> NumericType.of(isFloatingPoint = false, isUnsigned = isUnsigned, is8Byte = false)
				isUnsigned && (decimal.length > DECIMAL_ULONG_MAX.length || (decimal.length == DECIMAL_ULONG_MAX.length && decimal > DECIMAL_ULONG_MAX)) -> {
					numberValueIsOutOfRangeError(start)
				}
				
				!isUnsigned && (decimal.length > DECIMAL_LONG_MAX.length || (decimal.length == DECIMAL_LONG_MAX.length && decimal > DECIMAL_LONG_MAX)) -> {
					numberValueIsOutOfRangeError(start)
				}
				
				else -> NumericType.of(isFloatingPoint = false, isUnsigned = isUnsigned, is8Byte = true)
			}
		}
		val kind = NumberKind(decimal, NumberSystem.DECIMAL, type)
		return PzlToken(kind, start span position)
	}
	
	private const val HEX_UINT_MAX_LENGTH = 8
	private const val HEX_ULONG_MAX_LENGTH = 16
	
	context(_: FileContext)
	private fun parseHexPzlToken(input: CharArray, start: Int): PzlToken {
		var position = start + 2
		var is8Byte = false
		var isUnsigned = false
		val hex = buildString {
			while (true) {
				val char = input.getOrNull(position) ?: break
				when {
					char.isHex() -> {
						this.append(char)
						position++
					}
					
					char == 'U' || char == 'u' -> {
						isUnsigned = true
						position++
						val nextChar = input.getOrNull(position) ?: break
						if (nextChar == 'L') {
							is8Byte = true
							position++
						}
						break
					}
					
					char == 'L' -> {
						is8Byte = true
						position++
						break
					}
					
					char == 'F' || char == 'f' -> numberFormatError(position)
					
					else -> break
				}
			}
		}
		if (hex.isEmpty()) {
			numberFormatError(position)
		}
		if (!is8Byte && (isUnsigned && hex.length > HEX_UINT_MAX_LENGTH) || (!isUnsigned && hex.length >= HEX_UINT_MAX_LENGTH)) {
			is8Byte = true
		}
		if ((isUnsigned && hex.length > HEX_ULONG_MAX_LENGTH) || (!isUnsigned && hex.length >= HEX_ULONG_MAX_LENGTH)) {
			numberValueIsOutOfRangeError(start)
		}
		val type = NumericType.of(isFloatingPoint = false, isUnsigned = isUnsigned, is8Byte = is8Byte)
		val kind = NumberKind(hex, NumberSystem.HEX, type)
		return PzlToken(kind, start span position)
	}
	
	context(_: FileContext)
	private fun numberFormatError(position: Int): Nothing {
		lexicalError("数字格式错误", position)
	}
	
	context(_: FileContext)
	private fun numberValueIsOutOfRangeError(position: Int): Nothing {
		lexicalError("数值超出范围", position)
	}
}