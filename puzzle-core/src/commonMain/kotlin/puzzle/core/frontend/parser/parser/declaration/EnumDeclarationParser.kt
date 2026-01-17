package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.EnumDeclaration
import puzzle.core.frontend.ast.declaration.EnumEntry
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.core.frontend.parser.parser.type.SuperTypeTarget
import puzzle.core.frontend.parser.parser.type.parseSuperTypes
import puzzle.core.frontend.parser.parser.type.parseWithTypes
import puzzle.core.frontend.parser.parser.type.safeAsSuperTypeReferences
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACE
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SeparatorKind.SEMICOLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseEnumDeclaration(meta: DeclarationMeta, start: SourceLocation): EnumDeclaration {
	val name = parseIdentifier(IdentifierTarget.ENUM)
	val parameters = parseParameters(ParameterTarget.ENUM)
	val superTypes = parseSuperTypes(SuperTypeTarget.ENUM)
		.safeAsSuperTypeReferences()
	val withTypes = parseWithTypes()
	if (!cursor.match(LBRACE)) {
		val location = start span cursor.previous.location
		return EnumDeclaration(
			name = name,
			docComment = meta.docComment,
			modifiers = meta.modifiers,
			parameters = parameters,
			entries = emptyList(),
			superTypes = superTypes,
			withTypes = withTypes,
			typeSpec = meta.typeSpec,
			contextSpec = meta.contextSpec,
			annotationCalls = meta.annotationCalls,
			location = location
		)
	}
	val entries = parseEnumEntries()
	val info = when {
		cursor.match(RBRACE) -> MemberDeclarationInfo.Empty
		cursor.match(SEMICOLON) -> parseMemberDeclarationInfo(DeclarationMemberPolicy.ENUM)
		else -> syntaxError("enum 缺少 ';'", cursor.current)
	}
	val location = start span cursor.previous.location
	return EnumDeclaration(
		name = name,
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		parameters = parameters,
		entries = entries,
		superTypes = superTypes,
		withTypes = withTypes,
		typeSpec = meta.typeSpec,
		contextSpec = meta.contextSpec,
		annotationCalls = meta.annotationCalls,
		location = location,
		inits = info.inits,
		ctors = info.ctors,
		members = info.members,
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseEnumEntries(): List<EnumEntry> {
	return if (cursor.check(SEMICOLON)) emptyList() else buildList {
		do {
			this += parseEnumEntry()
		} while (cursor.match(COMMA))
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseEnumEntry(): EnumEntry {
	val name = parseIdentifier(IdentifierTarget.ENUM_ENTRY)
	val start = name.location
	if (cursor.match(LPAREN)) {
		while (!cursor.match(RPAREN)) {
			cursor.advance()
		}
	}
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo(DeclarationMemberPolicy.ENUM_ENTRY)
	} else MemberDeclarationInfo.Empty
	val end = cursor.previous.location
	if (info.ctors.isNotEmpty()) {
		syntaxError("枚举成员不允许有次构造函数", info.ctors.first())
	}
	return EnumEntry(
		name = name,
		members = info.members,
		inits = info.inits,
		location = start span end
	)
}