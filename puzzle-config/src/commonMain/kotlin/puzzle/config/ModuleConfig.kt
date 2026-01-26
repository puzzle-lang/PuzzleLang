package puzzle.config

import kotlinx.serialization.Serializable

@Serializable
class ModuleConfig(
	val name: String? = null,
	var version: String? = null,
	val group: String? = null,
	val ignore: List<String>? = null,
	val deps: List<String>? = null,
)