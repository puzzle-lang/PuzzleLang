package puzzle.token.kinds

sealed class WhiteSpaceKind(
	override val value: String,
) : PzlTokenKind {
	
	object NEWLINE : WhiteSpaceKind("NEWLINE")
	
	object WHITESPACE : WhiteSpaceKind("WHITESPACE")
	
	object TAB : WhiteSpaceKind("TAB")
}