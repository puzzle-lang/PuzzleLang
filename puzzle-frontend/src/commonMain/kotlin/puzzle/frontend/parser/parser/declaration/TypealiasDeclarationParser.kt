package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.TypeAliasDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.token.kinds.AssignmentKind.ASSIGN

context(_: FileContext, cursor: PzlTokenCursor)
fun parseTypeAliasDeclaration(meta: DeclarationMeta, start: SourceLocation): TypeAliasDeclaration {
	val name = parseIdentifier(IdentifierTarget.TYPEALIAS)
	cursor.expect(ASSIGN, "类型别名缺少 '='")
	val targetType = parseTypeReference(allowLambda = true)
	val end = cursor.previous.location
	return TypeAliasDeclaration(
		name = name,
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		typeSpec = meta.typeSpec,
		targetType = targetType,
		location = start span end
	)
}