package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.CtorDeclaration
import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.ast.declaration.InitDeclaration
import puzzle.core.frontend.ast.declaration.TopLevelAllowedDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.check
import puzzle.core.frontend.parser.matcher.declaration.member.MemberDeclarationMatcher
import puzzle.core.frontend.parser.matcher.declaration.toplevel.DeclarationMatcher
import puzzle.core.frontend.parser.parser.parameter.context.parseDeclarationContextSpec
import puzzle.core.frontend.parser.parser.parameter.parseErrorsSpec
import puzzle.core.frontend.parser.parser.parameter.type.parseTypeSpec
import puzzle.core.frontend.parser.parser.parseAnnotationCalls
import puzzle.core.frontend.parser.parser.parseDocComment
import puzzle.core.frontend.parser.parser.parseModifiers
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACE

context(_: FileContext, cursor: PzlTokenCursor)
fun parseDeclarations(): List<TopLevelAllowedDeclaration> {
	return buildList {
		while (!cursor.isAtEnd()) {
			this += parseDeclaration()
		}
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseDeclaration(): TopLevelAllowedDeclaration {
	val docComment = parseDocComment()
	val annotationCalls = parseAnnotationCalls()
	val typeSpec = parseTypeSpec()
	val contextSpec = parseDeclarationContextSpec()
	val errorsSpec = parseErrorsSpec()
	val modifiers = parseModifiers()
	val matcher = DeclarationMatcher.matchers.find { it.match() } ?: syntaxError(
		message = if (cursor.isAtEnd()) "结尾缺少 '}'" else "未知的顶层声明",
		token = cursor.current
	)
	val header = DeclarationMeta(docComment, annotationCalls, typeSpec, contextSpec, errorsSpec, modifiers)
	header.check(matcher.target, matcher.modifierTarget)
	val start = header.start
	return matcher.parse(header, start)
}

context(_: FileContext, cursor: PzlTokenCursor)
fun parseMemberDeclarationInfo(): MemberDeclarationInfo {
	val declarations = if (cursor.match(RBRACE)) emptyList() else {
		buildList {
			do {
				this += parseMemberDeclaration()
			} while (!cursor.match(RBRACE))
		}
	}
	return MemberDeclarationInfo(
		ctors = declarations.filterIsInstance<CtorDeclaration>(),
		inits = declarations.filterIsInstance<InitDeclaration>(),
		members = declarations.filterIsInstance<TopLevelAllowedDeclaration>()
	)
}

class MemberDeclarationInfo(
	val ctors: List<CtorDeclaration>,
	val inits: List<InitDeclaration>,
	val members: List<TopLevelAllowedDeclaration>,
) {
	
	companion object {
		
		val Empty = MemberDeclarationInfo(emptyList(), emptyList(), emptyList())
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseMemberDeclaration(): Declaration {
	val docComment = parseDocComment()
	val annotationCalls = parseAnnotationCalls()
	val typeSpec = parseTypeSpec()
	val contextSpec = parseDeclarationContextSpec()
	val errorsSpec = parseErrorsSpec()
	val modifiers = parseModifiers()
	val matcher = MemberDeclarationMatcher.matchers.find { it.match() } ?: syntaxError(
		message = if (cursor.isAtEnd()) "结尾缺少 '}'" else "未知的成员声明",
		token = cursor.current
	)
	val header = DeclarationMeta(docComment, annotationCalls, typeSpec, contextSpec, errorsSpec, modifiers)
	header.check(matcher.target, matcher.modifierTarget)
	val start = header.start
	return matcher.parse(header, start)
}

context(cursor: PzlTokenCursor)
private val DeclarationMeta.start: SourceLocation
	get() {
		if (docComment != null) return docComment.location
		if (annotationCalls.isNotEmpty()) return annotationCalls.first().location
		if (typeSpec != null) return typeSpec.location
		if (contextSpec != null) return contextSpec.location
		if (modifiers.isNotEmpty()) return modifiers.first().location
		return cursor.previous.location
	}