package puzzle.core.frontend.parser.parser

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.ImportDirective
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.declaration.parseDeclarations
import puzzle.core.frontend.token.kinds.NamespaceKind.IMPORT

context(file: FileContext, cursor: PzlTokenCursor)
fun parseFile(): AstFile {
	val start = cursor.current.location
	val packageDirective = parsePackageDirective()
	val importDirectives = mutableListOf<ImportDirective>()
	while (cursor.match(IMPORT)) {
		importDirectives += parseImportDirective()
	}
	val declarations = parseDeclarations()
	val sourcePath = file.path
	val end = cursor.previous.location
	return AstFile(
		name = sourcePath.name.removeSuffix(".pzl"),
		sourcePath = sourcePath,
		builtin = false,
		packageDirective = packageDirective,
		importDirectives = importDirectives,
		declarations = declarations,
		location = start span end
	)
}