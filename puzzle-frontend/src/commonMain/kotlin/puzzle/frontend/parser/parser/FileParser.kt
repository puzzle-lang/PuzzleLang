package puzzle.frontend.parser.parser

import puzzle.ast.AstFile
import puzzle.ast.ImportDirective
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.declaration.parseDeclarations
import puzzle.token.kinds.NamespaceKind.IMPORT

context(file: FileContext, cursor: PzlTokenCursor)
fun parseFile(): AstFile {
	val start = cursor.current.location
	val packageDirective = parsePackageDirective()
	val importDirectives = mutableListOf<ImportDirective>()
	while (cursor.match(IMPORT)) {
		importDirectives += parseImportDirective()
	}
	val declarations = parseDeclarations()
	val end = cursor.previous.location
	return AstFile(
		builtin = false,
		packageDirective = packageDirective,
		importDirectives = importDirectives,
		declarations = declarations,
		location = start span end
	)
}