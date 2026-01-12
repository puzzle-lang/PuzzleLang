package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.TypeAliasDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.AssignmentKind.ASSIGN

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