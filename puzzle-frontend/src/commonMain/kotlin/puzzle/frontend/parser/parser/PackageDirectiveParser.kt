package puzzle.frontend.parser.parser

import puzzle.ast.PackageDirective
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifierString
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.AccessKind.DOT
import puzzle.token.kinds.NamespaceKind.PACKAGE

context(file: FileContext, cursor: PzlTokenCursor)
fun parsePackageDirective(): PackageDirective {
    val startToken = cursor.current
    if (!cursor.match(PACKAGE)) {
        syntaxError("缺少 package", startToken)
    }
    val packages = mutableListOf<String>()
    packages += parseIdentifierString(IdentifierTarget.PACKAGE)
    while (cursor.match(DOT)) {
        packages += parseIdentifierString(IdentifierTarget.PACKAGE)
    }
    val group = file.parent.group
    if (packages.size >= group.size && packages.take(group.size) == group) {
        val end = cursor.previous.location
        return PackageDirective(packages, startToken.location span end)
    } else {
        val expect = group.joinToString(".")
        val actual = group.joinToString(".")
        syntaxError("包前缀与模块组路径不一致: 期望 '$expect', 实际为 '$actual'", startToken)
    }
}