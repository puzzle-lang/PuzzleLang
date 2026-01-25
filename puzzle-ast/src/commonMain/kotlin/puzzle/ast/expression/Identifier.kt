package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.diagnostic.source.SourceLocation

@Serializable
class Identifier(
	val value: String,
	override val location: SourceLocation,
) : Expression, DirectAssignable, CompoundAssignable {
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Identifier) return false
		return this.value == other.value
	}
	
	override fun hashCode(): Int {
		return value.hashCode()
	}
	
	override fun toString(): String {
		return value
	}
}

fun String.toIdentifier(
	location: SourceLocation = SourceLocation.Builtin,
): Identifier {
	return Identifier(this, location)
}