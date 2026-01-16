package puzzle.core.frontend.discovery

import puzzle.core.util.PathWrapper

class RootSource(
	val projectSources: List<ProjectSource>,
	val maxPathLength: Int,
)

class ProjectSource(
	val name: String,
	val path: PathWrapper,
	val moduleSources: List<ModuleSource>,
)

class ModuleSource(
	val name: String,
	val path: PathWrapper,
	val sourcePaths: List<PathWrapper>,
	val ignore: List<String>,
)