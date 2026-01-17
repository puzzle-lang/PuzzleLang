package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
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
fun parseMemberDeclarationInfo(
	policy: DeclarationMemberPolicy,
): MemberDeclarationInfo {
	val declarations = if (cursor.match(RBRACE)) emptyList() else {
		buildList {
			do {
				this += parseMemberDeclaration().also {
					it.checkMember(policy)
				}
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

context(_: FileContext)
private fun Declaration.checkMember(policy: DeclarationMemberPolicy) {
	val kind = when {
		!policy.allowProperty && this is PropertyDeclaration -> "属性"
		!policy.allowFun && this is FunDeclaration -> "函数"
		!policy.allowClass && this is ClassDeclaration -> "类"
		!policy.allowObject && this is ObjectDeclaration -> "单例对象"
		!policy.allowError && this is ErrorDeclaration -> "错误"
		!policy.allowTrait && this is TraitDeclaration -> "特征"
		!policy.allowMixin && this is MixinDeclaration -> "混入"
		!policy.allowStruct && this is StructDeclaration -> "结构体"
		!policy.allowEnum && this is EnumDeclaration -> "枚举"
		!policy.allowAnnotation && this is AnnotationDeclaration -> "注解"
		!policy.allowExtension && this is ExtensionDeclaration -> "扩展"
		!policy.allowTypeAlias && this is TypeAliasDeclaration -> "类型别名"
		!policy.allowCtor && this is CtorDeclaration -> "构造函数"
		!policy.allowInit && this is InitDeclaration -> "初始化块"
		else -> null
	}
	if (kind != null) {
		syntaxError("${policy.label}不支持成员$kind", this)
	}
}