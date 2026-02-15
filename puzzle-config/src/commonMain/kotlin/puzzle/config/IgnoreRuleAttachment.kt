package puzzle.config

import puzzle.core.context.ContextAttachment

class IgnoreRuleAttachment(
    val values: Set<IgnoreRule>,
) : ContextAttachment

data class IgnoreRule(
    val path: String,
    val kind: IgnoreKind,
    val raw: String,
)

enum class IgnoreKind(
    val value: String,
) {
    EXACT(""),
    CHILDREN("*"),
    RECURSIVE("**")
}