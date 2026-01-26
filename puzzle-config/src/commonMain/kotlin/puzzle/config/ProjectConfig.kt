package puzzle.config

import kotlinx.serialization.Serializable

@Serializable
class ProjectConfig(
	val name: String? = null,
	val version: String? = null,
	val modules: List<String>? = null,
	val entry: String? = null,
	val deps: List<String>? = null,
)