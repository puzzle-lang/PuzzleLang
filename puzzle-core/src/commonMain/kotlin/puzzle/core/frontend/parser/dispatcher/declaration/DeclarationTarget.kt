package puzzle.core.frontend.parser.dispatcher.declaration

enum class DeclarationTarget(
	val label: String,
	val allowType: Boolean,
	val allowTypeVariance: Boolean,
	val allowContext: Boolean,
	val allowErrors: Boolean,
) {
	FUN(
		label = "函数",
		allowType = true,
		allowTypeVariance = false,
		allowContext = true,
		allowErrors = true
	),
	PROPERTY(
		label = "属性",
		allowType = true,
		allowTypeVariance = false,
		allowContext = true,
		allowErrors = true,
	),
	CLASS(
		label = "类",
		allowType = true,
		allowTypeVariance = true,
		allowContext = true,
		allowErrors = true,
	),
	OBJECT(
		label = "单例对象",
		allowType = true,
		allowTypeVariance = true,
		allowContext = true,
		allowErrors = false
	),
	ERROR(
		label = "错误",
		allowType = true,
		allowTypeVariance = false,
		allowContext = false,
		allowErrors = false,
	),
	TRAIT(
		label = "特征",
		allowType = true,
		allowTypeVariance = true,
		allowContext = true,
		allowErrors = false,
	),
	MIXIN(
		label = "混入",
		allowType = true,
		allowTypeVariance = true,
		allowContext = true,
		allowErrors = false,
	),
	STRUCT(
		label = "结构体",
		allowType = true,
		allowTypeVariance = true,
		allowContext = false,
		allowErrors = false,
	),
	ENUM(
		label = "枚举",
		allowType = true,
		allowTypeVariance = true,
		allowContext = false,
		allowErrors = false
	),
	ANNOTATION(
		label = "注解",
		allowType = true,
		allowTypeVariance = false,
		allowContext = false,
		allowErrors = false
	),
	EXTENSION(
		label = "扩展",
		allowType = true,
		allowTypeVariance = true,
		allowContext = true,
		allowErrors = false,
	),
	TYPEALIAS(
		label = "类型别名",
		allowType = true,
		allowTypeVariance = false,
		allowContext = false,
		allowErrors = false,
	),
	CTOR(
		label = "构造函数",
		allowType = false,
		allowTypeVariance = false,
		allowContext = false,
		allowErrors = false,
	),
	INIT(
		label = "初始化块",
		allowType = false,
		allowTypeVariance = false,
		allowContext = false,
		allowErrors = false,
	)
}