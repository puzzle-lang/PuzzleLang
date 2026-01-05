package puzzle.core.frontend.parser.parser

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.declaration.ImportDeclaration
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.declaration.parseDeclarations
import puzzle.core.frontend.parser.parser.declaration.parseImportDeclaration
import puzzle.core.frontend.parser.parser.declaration.parsePackageDeclaration
import puzzle.core.frontend.token.kinds.NamespaceKind.IMPORT

context(context: PzlContext, cursor: PzlTokenCursor)
fun parseFile(): AstFile {
	val packageDeclaration = parsePackageDeclaration()
	val importDeclarations = mutableListOf<ImportDeclaration>()
	while (cursor.match(IMPORT)) {
		importDeclarations += parseImportDeclaration()
	}
	val declarations = parseDeclarations()
	val sourcePath = context.sourcePath
	return AstFile(
		name = sourcePath.name.removeSuffix(".pzl"),
		sourcePath = sourcePath,
		isBuiltin = false,
		packageDeclaration = packageDeclaration,
		importDeclarations = importDeclarations,
		declarations = declarations
	)
}