package puzzle.config

import puzzle.context.ContextAttachment

class DependenceAttachment(
	val value: MutableSet<Dependence>,
) : ContextAttachment

data class Dependence(
	val projectName: String,
	val moduleName: String,
)