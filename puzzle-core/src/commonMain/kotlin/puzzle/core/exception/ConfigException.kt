package puzzle.core.exception

class ConfigException(message: String) : PzlException(message)

fun configError(message: String, name: String? = null, value: String? = null, path: String? = null): Nothing {
	val message = buildString {
		append("错误:")
		if (name != null) {
			append(" $name")
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