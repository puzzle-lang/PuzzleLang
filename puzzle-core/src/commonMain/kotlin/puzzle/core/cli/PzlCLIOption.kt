package puzzle.core.cli

sealed interface PzlCLIOption

class PathOption(
	val path: String,
) : PzlCLIOption

class DebugFeaturesOption(
	val features: List<DebugFeature>,
) : PzlCLIOption

enum class DebugFeature(
	val value: String,
) {
	IGNORE_RULE("ignore-rule"),
	STATISTICAL_TIME("statistical-time"),
	OUTPUT_AST_JSON("output-ast-json")
}