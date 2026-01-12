package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.MixinDeclaration
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.type.parseNamedType
import puzzle.core.frontend.parser.parser.type.parseWithTypes
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.ContextualKind.ON
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA

context(_: FileContext, cursor: PzlTokenCursor)
fun parseMixinDeclaration(meta: DeclarationMeta, start: SourceLocation): MixinDeclaration {
	val name = parseIdentifier(IdentifierTarget.MIXIN)
	val mixinConstraints = parseMixinConstraints()
	val withTypes = parseWithTypes()
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo()
	} else MemberDeclarationInfo.Empty
	if (info.inits.isNotEmpty()) {
		syntaxError("混入不允许有初始化块", info.inits.first())
	}
	if (info.ctors.isNotEmpty()) {
		syntaxError("混入不允许有次构造函数", info.ctors.first())
	}
	val end = cursor.previous.location
	return MixinDeclaration(
		name = name,
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		typeSpec = meta.typeSpec,
		contextSpec = meta.contextSpec,
		annotationCalls = meta.annotationCalls,
		mixinConstraints = mixinConstraints,
		withTypes = withTypes,
		members = info.members,
		location = start span end
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseMixinConstraints(): List<NamedType> {
	if (!cursor.match(ON)) return emptyList()
	return buildList {
		do {
			this += parseNamedType()
		} while (cursor.match(COMMA))
	}
}