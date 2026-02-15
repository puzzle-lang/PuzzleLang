package puzzle.driver

import puzzle.core.exception.PzlException
import puzzle.core.exception.getExceptionMessage
import puzzle.core.io.FilePath

class ConfigException(message: String) : PzlException(message)

fun configError(message: String, name: String? = null, value: String? = null, path: FilePath? = null): Nothing {
    val message = buildString {
        if (name != null) {
            append(name)
            if (value != null) {
                append(": '$value'")
            }
        }
        append(" $message")
    }
    throw ConfigException(getExceptionMessage("配置错误", message, path))
}