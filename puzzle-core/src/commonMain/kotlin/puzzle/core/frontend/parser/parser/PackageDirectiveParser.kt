package puzzle.core.frontend.parser.parser

import puzzle.core.frontend.ast.PackageDirective
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifierString
import puzzle.core.frontend.token.kinds.AccessKind.DOT
import puzzle.core.frontend.token.kinds.NamespaceKind.PACKAGE

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