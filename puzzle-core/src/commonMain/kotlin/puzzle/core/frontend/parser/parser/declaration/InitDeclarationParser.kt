package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.frontend.ast.declaration.InitDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.statement.parseStatements
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE

context(_: FileContext, cursor: PzlTokenCursor)
fun parseInitDeclaration(meta: DeclarationMeta, start: SourceLocation): InitDeclaration {
	cursor.expect(LBRACE, "init 初始化块缺少 '{'")
	val body = parseStatements()
	val end = cursor.previous.location
	return InitDeclaration(meta.docComment, body, start span end)
}