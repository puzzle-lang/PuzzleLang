package puzzle.frontend.parser.parser.parameter.parameter

import puzzle.frontend.parser.parser.ModifierTarget

enum class ParameterTarget(
	val label: String,
	val modifierTarget: ModifierTarget,
	val allowLambda: Boolean,
	val allowWithoutParen: Boolean,
	val allowVarargQuantifier: Boolean,
	val allowProperty: Boolean,
	val requireVariability: Boolean,
) {
	FUN(
		label = "函数",
		modifierTarget = ModifierTarget.FUN_PARAMETER,
		allowLambda = true,
		allowWithoutParen = false,
		allowVarargQuantifier = true,
		allowProperty = false,
		requireVariability = false,
	),
	CLASS(
		label = "类",
		modifierTarget = ModifierTarget.CLASS_PARAMETER,
		allowLambda = true,
		allowWithoutParen = true,
		allowVarargQuantifier = true,
		allowProperty = true,
		requireVariability = false
	),
	OBJECT(
		label = "单例对象",
		modifierTarget = ModifierTarget.OBJECT_PARAMETER,
		allowLambda = true,
		allowWithoutParen = true,
		allowVarargQuantifier = true,
		allowProperty = true,
		requireVariability = false
	),
	ERROR(
		label = "错误",
		modifierTarget = ModifierTarget.ERROR_PARAMETER,
		allowLambda = false,
		allowWithoutParen = true,
		allowVarargQuantifier = false,
		allowProperty = true,
		requireVariability = true
	),
	ANNOTATION(
		label = "注解",
		modifierTarget = ModifierTarget.ANNOTATION_PARAMETER,
		allowLambda = false,
		allowWithoutParen = true,
		allowVarargQuantifier = true,
		allowProperty = true,
		requireVariability = true
	),
	ENUM(
		label = "枚举",
		modifierTarget = ModifierTarget.ENUM_PARAMETER,
		allowLambda = true,
		allowWithoutParen = true,
		allowVarargQuantifier = true,
		allowProperty = true,
		requireVariability = false
	),
	STRUCT(
		label = "结构体",
		modifierTarget = ModifierTarget.STRUCT_PARAMETER,
		allowLambda = false,
		allowWithoutParen = true,
		allowVarargQuantifier = false,
		allowProperty = true,
		requireVariability = true
	),
	CTOR(
		label = "次构造函数",
		modifierTarget = ModifierTarget.CTOR_PARAMETER,
		allowLambda = true,
		allowWithoutParen = false,
		allowVarargQuantifier = true,
		allowProperty = false,
		requireVariability = false
	)
}