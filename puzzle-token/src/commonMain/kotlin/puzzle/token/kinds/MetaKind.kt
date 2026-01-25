package puzzle.token.kinds

sealed class MetaKind(
	override val value: String,
) : PzlTokenKind {
	
	object EOF : MetaKind("EOF")
}