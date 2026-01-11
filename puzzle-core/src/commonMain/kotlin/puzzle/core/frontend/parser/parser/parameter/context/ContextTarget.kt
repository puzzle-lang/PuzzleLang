package puzzle.core.frontend.parser.parser.parameter.context

enum class ContextTarget(
	val label: String,
	val allowContext: Boolean,
) {
	FUN(
		label = "函数",
		allowContext = true
	),
	PROPERTY(
		label = "属性",
		allowContext = true
	),
	CLASS(
		label = "类",
		allowContext = true
	),
	OBJECT(
		label = "单例对象",
		allowContext = true
	),
	TRAIT(
		label = "特征",
		allowContext = true
	),
	MIXIN(
		label = "混入",
		allowContext = true
	),
	STRUCT(
		label = "结构体",
		allowContext = false
	),
	ENUM(
		label = "枚举",
		allowContext = false
	),
	ANNOTATION(
		label = "注解",
		allowContext = false
	),
	EXTENSION(
		label = "扩展",
		allowContext = false
	),
	TYPEALIAS(
		label = "类型别名",
		allowContext = false
	),
	CTOR(
		label = "次构造函数",
		allowContext = false
	),
	INIT(
		label = "初始化块",
		allowContext = false
	)
}