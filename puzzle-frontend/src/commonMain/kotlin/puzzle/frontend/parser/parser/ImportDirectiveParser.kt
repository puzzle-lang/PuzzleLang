package puzzle.frontend.parser.parser

import puzzle.ast.ImportDirective
import puzzle.ast.ImportKind
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.matchIdentifier
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.expression.parseIdentifierString
import puzzle.token.kinds.AccessKind.DOT
import puzzle.token.kinds.OperatorKind.DOUBLE_STAR
import puzzle.token.kinds.OperatorKind.STAR
import puzzle.token.kinds.TypeOperatorKind.AS

context(_: FileContext, cursor: PzlTokenCursor)
fun parseImportDirective(): ImportDirective {
	val start = cursor.previous.location
	val name = parseIdentifierString(IdentifierTarget.IMPORT)
	val segments = mutableListOf(name)
	var scope = ImportKind.SINGLE
	var alias: Identifier? = null
	while (cursor.match(DOT)) {
		when {
			cursor.matchIdentifier() -> {
				segments += cursor.previous.value
				if (cursor.match(AS)) {
					alias = parseIdentifier(IdentifierTarget.IMPORT_AS)
					break
				}
			}
			
			cursor.match(STAR) -> {
				scope = ImportKind.WILDCARD
				break
			}
			
			cursor.match(DOUBLE_STAR) -> {
				scope = ImportKind.RECURSIVE
				break
			}
		}
	}
	val end = cursor.previous.location
	return ImportDirective(
		segments = segments,
		alias = alias,
		kind = scope,
		location = start span end
	)
}