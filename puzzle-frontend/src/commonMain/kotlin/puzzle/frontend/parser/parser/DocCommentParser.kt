package puzzle.frontend.parser.parser

import puzzle.ast.DocComment
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.token.kinds.CommentKind.Doc

context(cursor: PzlTokenCursor)
fun parseDocComment(): DocComment? {
	return if (cursor.match { it.kind is Doc }) {
		val token = cursor.previous
		DocComment(token.value, token.location)
	} else null
}