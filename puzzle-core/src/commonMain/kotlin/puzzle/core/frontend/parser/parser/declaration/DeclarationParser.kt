package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.CtorDeclaration
import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.ast.declaration.InitDeclaration
import puzzle.core.frontend.ast.declaration.TopLevelAllowedDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.check
import puzzle.core.frontend.parser.dispatcher.declaration.member.*
import puzzle.core.frontend.parser.dispatcher.declaration.toplevel.*
import puzzle.core.frontend.parser.parser.parameter.context.parseDeclarationContextSpec
import puzzle.core.frontend.parser.parser.parameter.parseErrorsSpec
import puzzle.core.frontend.parser.parser.parameter.type.parseTypeSpec
import puzzle.core.frontend.parser.parser.parseAnnotationCalls
import puzzle.core.frontend.parser.parser.parseDocComment
import puzzle.core.frontend.parser.parser.parseModifiers
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACE
import puzzle.core.frontend.token.kinds.ContextualKind.INIT
import puzzle.core.frontend.token.kinds.DeclarationKind.*
import puzzle.core.frontend.token.kinds.ModifierKind.VAL
import puzzle.core.frontend.token.kinds.ModifierKind.VAR

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
	val dispatcher = parseDeclarationDispatcher()
	cursor.advance()
	val meta = DeclarationMeta(docComment, annotationCalls, typeSpec, contextSpec, errorsSpec, modifiers)
	meta.check(dispatcher.target, dispatcher.modifierTarget)
	val start = meta.start
	return dispatcher.parse(meta, start)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseDeclarationDispatcher(): DeclarationDispatcher<*> {
	return when (cursor.current.kind) {
		FUN -> FunDeclarationDispatcher
		VAR, VAL -> PropertyDeclarationDispatcher
		CLASS -> ClassDeclarationDispatcher
		OBJECT -> ObjectDeclarationDispatcher
		ERROR -> ErrorDeclarationDispatcher
		TRAIT -> TraitDeclarationDispatcher
		MIXIN -> MixinDeclarationDispatcher
		STRUCT -> StructDeclarationDispatcher
		ENUM -> EnumDeclarationDispatcher
		ANNOTATION -> AnnotationDeclarationDispatcher
		EXTENSION -> ExtensionDeclarationDispatcher
		TYPEALIAS -> TypeAliasDeclarationDispatcher
		else -> syntaxError(
			message = if (cursor.isAtEnd()) "结尾缺少 '}'" else "未知的顶层声明",
			token = cursor.current
		)
	}
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
	val dispatcher = parseMemberDeclarationDispatcher()
	cursor.advance()
	val meta = DeclarationMeta(docComment, annotationCalls, typeSpec, contextSpec, errorsSpec, modifiers)
	meta.check(dispatcher.target, dispatcher.modifierTarget)
	val start = meta.start
	return dispatcher.parse(meta, start)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseMemberDeclarationDispatcher(): MemberDeclarationDispatcher<*> {
	return when (cursor.current.kind) {
		FUN -> MemberFunDeclarationDispatcher
		VAR, VAL -> MemberPropertyDeclarationDispatcher
		CLASS -> MemberClassDeclarationDispatcher
		OBJECT -> MemberObjectDeclarationDispatcher
		ERROR -> MemberErrorDeclarationDispatcher
		TRAIT -> MemberTraitDeclarationDispatcher
		MIXIN -> MemberMixinDeclarationDispatcher
		STRUCT -> MemberStructDeclarationDispatcher
		ENUM -> MemberEnumDeclarationDispatcher
		ANNOTATION -> MemberAnnotationDeclarationDispatcher
		EXTENSION -> MemberExtensionDeclarationDispatcher
		TYPEALIAS -> MemberTypeAliasDeclarationDispatcher
		CTOR -> MemberCtorDeclarationDispatcher
		INIT -> MemberInitDeclarationDispatcher
		else -> syntaxError(
			message = if (cursor.isAtEnd()) "结尾缺少 '}'" else "未知的顶层声明",
			token = cursor.current
		)
	}
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