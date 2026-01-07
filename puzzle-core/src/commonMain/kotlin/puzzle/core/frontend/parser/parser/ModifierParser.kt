package puzzle.core.frontend.parser.parser

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.token.kinds.ModifierKind
import puzzle.core.frontend.token.kinds.ModifierKind.*
import puzzle.core.frontend.token.kinds.SymbolTokenKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseModifiers(): List<Modifier> {
	val modifiers = buildList {
		while (true) {
			val modifier = parseModifier() ?: break
			val kind = modifier.kind
			if (kind == VAR || kind == VAR) break
			this += modifier
		}
	}
	checkModifierOrder(modifiers)
	return modifiers
}

context(cursor: PzlTokenCursor)
private fun parseModifier(): Modifier? {
	ModifierKind.availableKinds.fastForEach { kind ->
		if (cursor.current.kind == kind && cursor.nextOrNull?.kind != COLON) {
			cursor.advance()
			return Modifier(kind, cursor.previous.location)
		}
	}
	return null
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun checkModifierOrder(modifiers: List<Modifier>) {
	if (modifiers.isEmpty()) return
	var last: Modifier? = null
	var lastOrder = -1
	modifiers.forEachIndexed { index, current ->
		when {
			current.kind.order > lastOrder -> {
				lastOrder = current.kind.order
				last = current
			}
			
			current.kind.order < lastOrder -> syntaxError(
				message = "不规范的修饰符顺序, '${current.kind.value}' 需要在 '${last!!.kind.value}' 前面",
				token = cursor.offset(offset = -modifiers.size + index)
			)
			
			else -> syntaxError(
				message = "'${last!!.kind.value}' 和 '${current.kind.value}' 修饰符不允许同时使用",
				token = cursor.offset(offset = -modifiers.size + index)
			)
		}
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun List<Modifier>.check(target: ModifierTarget) {
	this.forEachIndexed { index, modifier ->
		if (modifier.kind !in target.supportedModifiers) {
			syntaxError(
				message = "${target.label}不支持 '${modifier.kind.value}' 修饰符",
				token = cursor.offset(offset = -this.size + index - 1)
			)
		}
	}
	if (this.all { it.kind != FINAL }) return
	if (this.all { it.kind != OVERRIDE }) {
		syntaxError(
			"‘final’ 修饰符必须配合 'override' 修饰符使用",
			cursor.offset(offset = -this.size + this.indexOfFirst { it.kind == FINAL } - 1)
		)
	}
}

enum class ModifierTarget(
	val label: String,
	val supportedModifiers: Set<ModifierKind>,
) {
	FUN(
		label = "函数",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST, PREFIX, POSTFIX)
	),
	PROPERTY(
		label = "属性",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST, LATE, LAZY, VAR, VAL)
	),
	CLASS(
		label = "类",
		supportedModifiers = TopLevelAccessModifiers + setOf(OPEN, ABSTRACT, SEALED)
	),
	OBJECT(
		label = "单例对象",
		supportedModifiers = TopLevelAccessModifiers
	),
	TRAIT(
		label = "特征",
		supportedModifiers = TopLevelAccessModifiers + setOf(SEALED)
	),
	MIXIN(
		label = "混入",
		supportedModifiers = TopLevelAccessModifiers
	),
	STRUCT(
		label = "结构体",
		supportedModifiers = TopLevelAccessModifiers
	),
	ENUM(
		label = "枚举",
		supportedModifiers = TopLevelAccessModifiers
	),
	ANNOTATION(
		label = "注解",
		supportedModifiers = TopLevelAccessModifiers
	),
	EXTENSION(
		label = "扩展",
		supportedModifiers = TopLevelAccessModifiers
	),
	TYPEALIAS(
		label = "类型别名",
		supportedModifiers = TopLevelAccessModifiers
	),
	MEMBER_FUN(
		label = "成员函数",
		supportedModifiers = MemberAccessModifiers + setOf(FINAL, OVERRIDE, OPEN, ABSTRACT, PREFIX, POSTFIX)
	),
	MEMBER_PROPERTY(
		label = "属性",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST, LATE, LAZY, VAR, VAL)
	),
	MEMBER_CLASS(
		label = "成员类",
		supportedModifiers = MemberAccessModifiers + setOf(INNER, OPEN, ABSTRACT)
	),
	MEMBER_OBJECT(
		label = "成员单例对象",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_TRAIT(
		label = "成员特征",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_MIXIN(
		label = "成员混入",
		supportedModifiers = TopLevelAccessModifiers
	),
	MEMBER_STRUCT(
		label = "成员结构体",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_ENUM(
		label = "成员枚举",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_ANNOTATION(
		label = "成员注解",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_EXTENSION(
		label = "成员扩展",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_TYPEALIAS(
		label = "成员类型别名",
		supportedModifiers = MemberAccessModifiers
	),
	CTOR(
		label = "次构造函数",
		supportedModifiers = TopLevelAccessModifiers + setOf(FILE)
	),
	INIT(
		label = "初始化块",
		supportedModifiers = emptySet()
	),
	FUN_PARAMETER(
		label = "函数参数",
		supportedModifiers = setOf(VAR)
	),
	CTOR_PARAMETER(
		label = "次构造函数参数",
		supportedModifiers = setOf(VAR)
	),
	CLASS_PARAMETER(
		label = "类参数",
		supportedModifiers = MemberAccessModifiers + setOf(OPEN, ABSTRACT, VAR, VAL),
	),
	OBJECT_PARAMETER(
		label = "单例对象参数",
		supportedModifiers = MemberAccessModifiers + setOf(VAR, VAL),
	),
	STRUCT_PARAMETER(
		label = "结构体参数",
		supportedModifiers = MemberAccessModifiers + setOf(VAR, VAL, IGNORE)
	),
	ENUM_PARAMETER(
		label = "枚举参数",
		supportedModifiers = MemberAccessModifiers + setOf(VAR, VAL)
	),
	ANNOTATION_PARAMETER(
		label = "注解参数",
		supportedModifiers = setOf(VAL)
	)
}

private val MemberAccessModifiers = setOf<ModifierKind>(PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC)

private val TopLevelAccessModifiers = setOf<ModifierKind>(PRIVATE, INTERNAL, MODULE, PUBLIC)