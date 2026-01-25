package puzzle.token.kinds

sealed interface CommentKind : PzlTokenKind {
	
	class SingleLine(override val value: String) : CommentKind
	
	class MultiLine(override val value: String) : CommentKind
	
	class Doc(override val value: String) : CommentKind
}