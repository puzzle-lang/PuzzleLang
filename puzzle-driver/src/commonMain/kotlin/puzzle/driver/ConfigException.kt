package puzzle.driver

import puzzle.base.exception.PzlException

class ConfigException(message: String) : PzlException(message)

fun configError(message: String, name: String? = null, value: String? = null, path: String? = null): Nothing {
	val message = buildString {
		if (name != null) {
			append(name)
			if (value != null) {
				append(": '$value'")
			}
		}
		append(" $message")
		if (path != null) {
			append("\n错误位置: $path")
		}
	}
	throw ConfigException(message)
}