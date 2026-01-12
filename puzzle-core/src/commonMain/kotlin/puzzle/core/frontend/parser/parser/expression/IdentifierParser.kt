package puzzle.core.frontend.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.PzlToken
import puzzle.core.frontend.token.kinds.IdentifierKind
import puzzle.core.frontend.token.kinds.KeywordKind

context(_: FileContext, cursor: PzlTokenCursor)
fun parseIdentifier(target: IdentifierTarget): Identifier {
	return tryParseIdentifier(target)
		?: syntaxError("${target.label}缺少名称", cursor.current)
}

context(_: FileContext, cursor: PzlTokenCursor)
fun tryParseIdentifier(target: IdentifierTarget): Identifier? {
	return tryParseIdentifierString(target)?.let {
		Identifier(it, cursor.previous.location)
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
fun parseIdentifierString(target: IdentifierTarget): String {
	return tryParseIdentifierString(target)
		?: syntaxError("${target.label}缺少名称", cursor.current)
}

context(_: FileContext, cursor: PzlTokenCursor)
fun tryParseIdentifierString(target: IdentifierTarget): String? {
	if (cursor.match { it.kind is IdentifierKind }) {
		val value = cursor.previous.value
		if (value == "_" && !target.allowAnonymousBinding) {
			syntaxError("${target.label}不支持匿名绑定", cursor.previous)
		}
		return value
	}
	if (cursor.match { it.kind.value in KeywordKind.softKeywords }) {
		return cursor.previous.value
	}
	return null
}

fun PzlToken.toIdentifier(allowAnonymousBinding: Boolean = false): Identifier {
	if (this.isIdentifier(allowAnonymousBinding)) {
		return Identifier(kind.value, this.location)
	}
	error("不支持转换为标识符")
}

fun PzlToken.isIdentifier(allowAnonymousBinding: Boolean = false): Boolean {
	return this.kind is IdentifierKind || this.kind.value in KeywordKind.softKeywords || (this.kind.value == "_" && allowAnonymousBinding)
}

fun PzlTokenCursor.checkIdentifier(allowAnonymousBinding: Boolean = false): Boolean {
	return this.check { it.isIdentifier(allowAnonymousBinding) }
}

fun PzlTokenCursor.matchIdentifier(allowAnonymousBinding: Boolean = false): Boolean {
	return this.match { it.isIdentifier(allowAnonymousBinding) }
}

enum class IdentifierTarget(
	val label: String,
	val allowAnonymousBinding: Boolean,
) {
	FUN(
		label = "函数声明",
		allowAnonymousBinding = false,
	),
	PROPERTY(
		label = "属性声明",
		allowAnonymousBinding = false,
	),
	PROPERTY_DESTRUCTURE(
		label = "属性解构声明",
		allowAnonymousBinding = true,
	),
	CLASS(
		label = "类声明",
		allowAnonymousBinding = false,
	),
	OBJECT(
		label = "单例对象声明",
		allowAnonymousBinding = false
	),
	ERROR(
		label = "错误声明",
		allowAnonymousBinding = false
	),
	TRAIT(
		label = "特征声明",
		allowAnonymousBinding = false,
	),
	MIXIN(
		label = "混入声明",
		allowAnonymousBinding = false
	),
	STRUCT(
		label = "结构体声明",
		allowAnonymousBinding = false,
	),
	ANNOTATION(
		label = "注解声明",
		allowAnonymousBinding = false
	),
	TYPEALIAS(
		label = "类型别名",
		allowAnonymousBinding = false
	),
	ENUM(
		label = "枚举声明",
		allowAnonymousBinding = false
	),
	ENUM_ENTRY(
		label = "枚举成员声明",
		allowAnonymousBinding = false,
	),
	CTOR(
		label = "次构造函数声明",
		allowAnonymousBinding = false
	),
	CONTEXT_RECEIVER(
		label = "上下文型参",
		allowAnonymousBinding = true
	),
	TYPE_PARAMETER(
		label = "泛型型参",
		allowAnonymousBinding = false
	),
	PARAMETER(
		label = "型参",
		allowAnonymousBinding = false,
	),
	LAMBDA_PARAMETER(
		label = "lambda 型参",
		allowAnonymousBinding = false,
	),
	LAMBDA_PARAMETER_REFERENCE(
		label = "lambda 参数引用",
		allowAnonymousBinding = true,
	),
	FOR_PARAMETER_REFERENCE(
		label = "for 参数引用",
		allowAnonymousBinding = true,
	),
	VARIABLE(
		label = "变量",
		allowAnonymousBinding = true
	),
	VARIABLE_DESTRUCTURE(
		label = "解构变量",
		allowAnonymousBinding = true,
	),
	TYPE_REFERENCE(
		label = "类型",
		allowAnonymousBinding = false
	),
	PACKAGE(
		label = "包",
		allowAnonymousBinding = false,
	),
	IMPORT(
		label = "导入",
		allowAnonymousBinding = false,
	),
	IMPORT_AS(
		label = "符号别名",
		allowAnonymousBinding = false,
	),
	MATCH_AS(
		label = "模式绑定名",
		allowAnonymousBinding = false,
	),
	ACCESS_OPERATOR(
		label = "成员访问",
		allowAnonymousBinding = false,
	),
	ARGUMENT(
		label = "实参",
		allowAnonymousBinding = false,
	),
	TYPE_ARGUMENT(
		label = "泛型实参",
		allowAnonymousBinding = false
	),
	LABEL(
		label = "标签",
		allowAnonymousBinding = false
	),
	GETTER_PARAMETER(
		label = "属性访问器参数",
		allowAnonymousBinding = false,
	),
	SETTER_PARAMETER(
		label = "属性赋值器参数",
		allowAnonymousBinding = false,
	)
}