package puzzle.frontend.parser.parser

import puzzle.ast.PackageDirective
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifierString
import puzzle.token.kinds.AccessKind.DOT
import puzzle.token.kinds.NamespaceKind.PACKAGE

context(_: FileContext, cursor: PzlTokenCursor)
fun parsePackageDirective(): PackageDirective? {
	if (!cursor.match(PACKAGE)) return null
	val start = cursor.previous.location
	val packages = mutableListOf<String>()
	packages += parseIdentifierString(IdentifierTarget.PACKAGE)
	while (cursor.match(DOT)) {
		packages += parseIdentifierString(IdentifierTarget.PACKAGE)
	}
	val end = cursor.previous.location
	return PackageDirective(packages, start span end)
}