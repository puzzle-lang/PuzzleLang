package puzzle.core.frontend.discovery

import puzzle.core.util.PathWrapper

class ProjectSource(
	val name: String,
	val moduleSources: List<ModuleSource>,
	val maxPathLength: Int,
)

class ModuleSource(
	val name: String,
	val path: PathWrapper,
	val sourcePaths: List<PathWrapper>,
)