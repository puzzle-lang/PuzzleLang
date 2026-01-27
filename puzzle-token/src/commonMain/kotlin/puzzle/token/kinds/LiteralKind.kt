package puzzle.token.kinds

import puzzle.core.collection.fastSetOf
import puzzle.core.location.SourceLocation
import puzzle.token.PzlToken

sealed interface LiteralKind : PzlTokenKind {
	
	companion object {
		
		val keywordKinds = BooleanKind.kinds + NULL
	}
	
	object NULL : LiteralKind, KeywordKind {
		
		override val value = "null"
	}
}

sealed class BooleanKind(
	override val value: String,
) : LiteralKind, KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf(TRUE, FALSE)
	}
	
	object TRUE : BooleanKind("true")
	
	object FALSE : BooleanKind("false")
}

class CharKind(
	override val value: String,
) : LiteralKind

class NumberKind(
	override val value: String,
	val system: NumberSystem,
	val type: NumericType,
) : LiteralKind

enum class NumberSystem {
	BINARY,
	DECIMAL,
	HEX
}

enum class NumericType(
	val isFloatingPoint: Boolean,
	val isUnsigned: Boolean,
	val is8Byte: Boolean,
) {
	INT(isFloatingPoint = false, isUnsigned = false, is8Byte = false),
	LONG(isFloatingPoint = false, isUnsigned = false, is8Byte = true),
	UINT(isFloatingPoint = false, isUnsigned = true, is8Byte = false),
	ULONG(isFloatingPoint = false, isUnsigned = true, is8Byte = true),
	FLOAT(isFloatingPoint = true, isUnsigned = false, is8Byte = false),
	DOUBLE(isFloatingPoint = true, isUnsigned = false, is8Byte = true);
	
	companion object {
		
		fun of(isFloatingPoint: Boolean, isUnsigned: Boolean, is8Byte: Boolean): NumericType = entries.first {
			it.isFloatingPoint == isFloatingPoint && it.isUnsigned == isUnsigned && it.is8Byte == is8Byte
		}
	}
}

sealed interface StringKind : LiteralKind {
	
	class Text(
		override val value: String,
	) : StringKind
	
	class Template(
		val parts: List<Part>,
	) : StringKind {
		
		override val value by lazy {
			parts.joinToString("") { part ->
				when (part) {
					is Part.Expression -> "[EXPRESSION]"
					is Part.Text -> part.value
				}
			}
		}
		
		sealed interface Part {
			
			val location: SourceLocation
			
			class Text(
				val value: String,
				override val location: SourceLocation,
			) : Part
			
			class Expression(
				val tokens: List<PzlToken>,
				override val location: SourceLocation,
			) : Part
		}
	}
}