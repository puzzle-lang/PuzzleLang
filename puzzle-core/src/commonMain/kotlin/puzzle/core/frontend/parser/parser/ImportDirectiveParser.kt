package puzzle.core.frontend.parser.parser

import puzzle.core.frontend.ast.ImportDirective
import puzzle.core.frontend.ast.ImportScope
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.matchIdentifier
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.expression.parseIdentifierString
import puzzle.core.frontend.token.kinds.AccessKind.DOT
import puzzle.core.frontend.token.kinds.OperatorKind.DOUBLE_STAR
import puzzle.core.frontend.token.kinds.OperatorKind.STAR
import puzzle.core.frontend.token.kinds.TypeOperatorKind.AS

context(_: FileContext, cursor: PzlTokenCursor)
fun parseImportDirective(): ImportDirective {
	val start = cursor.previous.location
	val name = parseIdentifierString(IdentifierTarget.IMPORT)
	val segments = mutableListOf(name)
	var scope = ImportScope.SINGLE
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
				scope = ImportScope.WILDCARD
				break
			}
			
			cursor.match(DOUBLE_STAR) -> {
				scope = ImportScope.RECURSIVE
				break
			}
		}
	}
	val end = cursor.previous.location
	return ImportDirective(
		segments = segments,
		alias = alias,
		scope = scope,
		location = start span end
	)
}