package puzzle.config

import puzzle.core.context.ContextAttachment

class DependenceAttachment(
    val values: MutableList<Dependence>,
) : ContextAttachment

data class Dependence(
    val projectName: String,
    val moduleName: String,
)