package puzzle.core.util

inline fun <reified T> List<*>.containsType(): Boolean = any { it is T }