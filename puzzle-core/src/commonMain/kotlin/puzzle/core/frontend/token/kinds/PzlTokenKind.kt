package puzzle.core.frontend.token.kinds

sealed interface PzlTokenKind {
	
	val value: String
}

class IdentifierKind(
	override val value: String,
) : PzlTokenKind