package puzzle.core.frontend.discovery

import puzzle.core.util.PathWrapper

class ProjectSource(
	val name: String,
	val modules: List<ModuleSource>,
	val maxPathLength: Int
)

class ModuleSource(
	val name: String,
	val paths: List<PathWrapper>,
)