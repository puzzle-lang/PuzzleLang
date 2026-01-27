package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.InitDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.parser.statement.parseStatements
import puzzle.token.kinds.BracketKind.Start.LBRACE

context(_: FileContext, cursor: PzlTokenCursor)
fun parseInitDeclaration(meta: DeclarationMeta, start: SourceLocation): InitDeclaration {
	cursor.expect(LBRACE, "init 初始化块缺少 '{'")
	val body = parseStatements()
	val end = cursor.previous.location
	return InitDeclaration(meta.docComment, body, start span end)
}