package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.TraitDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.type.SuperTypeTarget
import puzzle.core.frontend.parser.parser.type.parseSuperTypes
import puzzle.core.frontend.parser.parser.type.safeAsSuperTypeReferences
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.ContextualKind.WITH

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