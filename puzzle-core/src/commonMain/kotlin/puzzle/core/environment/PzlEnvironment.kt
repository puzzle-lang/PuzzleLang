package puzzle.core.environment

import puzzle.core.environment.EnvironmentOption.*

object PzlEnvironment {


    private val options = mutableMapOf<EnvironmentOption, Any>()

    fun init(options: Map<EnvironmentOption, Any>) {
        if (this.options.isNotEmpty()) {
            error("重复初始化了")
        }
        this.options += options
    }

    val projectPath by lazy { options.getString(PROJECT_PATH) }

    val enableExportAst by lazy { options.getBoolean(EXPORT_AST) }

    val enableAnsiColor by lazy { options.getBoolean(ANSI_COLOR) }

    val enableStackTrace by lazy { options.getBoolean(STACK_TRACE) }

    val enableInfoProgress by lazy { options.getBoolean(INFO_PROGRESS) }

    val enableInfoIgnore by lazy { options.getBoolean(INFO_IGNORE) }

    val enableInfoFile by lazy { options.getBoolean(INFO_FILE) }

    private fun Map<EnvironmentOption, Any>.getString(
        key: EnvironmentOption,
        default: String = ""
    ): String {
        return this[key] as? String ?: default
    }

    private fun Map<EnvironmentOption, Any>.getBoolean(
        key: EnvironmentOption,
        default: Boolean = false
    ): Boolean {
        return this[key] as? Boolean ?: default
    }
}

enum class EnvironmentOption {
    PROJECT_PATH,
    EXPORT_AST,
    ANSI_COLOR,
    STACK_TRACE,
    INFO_PROGRESS,
    INFO_IGNORE,
    INFO_FILE
}