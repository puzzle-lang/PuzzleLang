package puzzle.core.frontend.discovery

import kotlinx.serialization.Serializable

@Serializable
class ProjectConfig(
	val name: String? = null,
	val version: String? = null,
	val modules: List<String>? = null,
	val entry: String? = null,
	val deps: List<String>? = null,
)

@Serializable
class ModuleConfig(
	val name: String? = null,
	var version: String? = null,
	val group: String? = null,
	val ignore: List<String>? = null,
	val deps: List<String>? = null,
)

data class IgnoreRule(
	val path: String,
	val kind: IgnoreKind,
)

enum class IgnoreKind(
	val value: String,
) {
	EXACT(""),
	CHILDREN("*"),
	RECURSIVE("**")
}