package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.TraitDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.type.SuperTypeTarget
import puzzle.frontend.parser.parser.type.parseSuperTypes
import puzzle.frontend.parser.parser.type.safeAsSuperTypeReferences
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.ContextualKind.WITH

context(_: FileContext, cursor: PzlTokenCursor)
fun parseTraitDeclaration(meta: DeclarationMeta, start: SourceLocation): TraitDeclaration {
	val name = parseIdentifier(IdentifierTarget.TRAIT)
	val superTypes = parseSuperTypes(SuperTypeTarget.TRAIT)
		.safeAsSuperTypeReferences()
	if (cursor.match(WITH)) {
		syntaxError("结构体不支持 with", cursor.previous)
	}
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo(DeclarationMemberPolicy.TRAIT)
	} else MemberDeclarationInfo.Empty
	val end = cursor.previous.location
	return TraitDeclaration(
		name = name,
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		typeSpec = meta.typeSpec,
		contextSpec = meta.contextSpec,
		annotationCalls = meta.annotationCalls,
		superTypes = superTypes,
		members = info.members,
		location = start span end
	)
}