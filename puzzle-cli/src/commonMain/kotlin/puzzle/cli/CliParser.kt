package puzzle.cli

import puzzle.core.environment.EnvironmentOption
import puzzle.core.environment.PzlEnvironment

private val availableArgParseMap = mapOf(
    "--path" to ::parsePathOption,
    "--features" to ::parseFeaturesOption,
    "--exports" to ::parseExportsOption,
    "--infos" to ::parseInfosOption
)

private val allOptions = mutableMapOf<EnvironmentOption, Any>()

fun parseCliOptions(options: List<String>) {
    val usedArgTypes = mutableSetOf<String>()
    val argKeys = availableArgParseMap.keys
    options.forEach { arg ->
        val key = argKeys.find { arg.startsWith("$it=") }
            ?: cliError("未知选项: ${arg.split("=").first()}")
        if (key in usedArgTypes) {
            cliError("重复的选项: $key")
        }
        usedArgTypes += key
        val value = arg.removePrefix("$key=")
        availableArgParseMap[key]!!(value)
    }
    checkOption()
    PzlEnvironment.init(allOptions)
}

private fun checkOption() {
    if (allOptions[EnvironmentOption.PROJECT_PATH] == null) {
        cliError("缺少 --path 选项")
    }
}

private fun parsePathOption(value: String) {
    allOptions[EnvironmentOption.PROJECT_PATH] = value
}

private val availableFeatureOptions = setOf(
    "ansi-color",
    "error-stack"
)

private fun parseFeaturesOption(value: String) {
    parseAndCheckOptions(
        value = value,
        key = "--features",
        availableOptions = availableFeatureOptions,
        onAll = {
            allOptions[EnvironmentOption.ANSI_COLOR] = true
            allOptions[EnvironmentOption.STACK_TRACE] = true
        },
        onAction = {
            allOptions[EnvironmentOption.ANSI_COLOR] = "ansi-color" in it
            allOptions[EnvironmentOption.STACK_TRACE] = "stack-trace" in it
        }
    )
}

private val availableExportOptions = setOf(
    "ast"
)

private fun parseExportsOption(value: String) {
    parseAndCheckOptions(
        value = value,
        key = "--exports",
        availableOptions = availableExportOptions,
        onAll = {
            allOptions[EnvironmentOption.EXPORT_AST] = true
        },
        onAction = {
            allOptions[EnvironmentOption.EXPORT_AST] = "ast" in it
        }
    )
}

private val availableInfoOptions = setOf(
    "progress",
    "ignore",
    "file"
)

private fun parseInfosOption(value: String) {
    parseAndCheckOptions(
        value = value,
        key = "--infos",
        availableOptions = availableInfoOptions,
        onAll = {
            allOptions[EnvironmentOption.INFO_PROGRESS] = true
            allOptions[EnvironmentOption.INFO_IGNORE] = true
            allOptions[EnvironmentOption.INFO_FILE] = true
        },
        onAction = {
            allOptions[EnvironmentOption.INFO_PROGRESS] = "progress" in it
            allOptions[EnvironmentOption.INFO_IGNORE] = "ignore" in it
            allOptions[EnvironmentOption.INFO_FILE] = "file" in it
        }
    )
}

private fun parseAndCheckOptions(
    value: String,
    key: String,
    availableOptions: Set<String>,
    onAll: () -> Unit,
    onAction: (Set<String>) -> Unit,
) {
    if (value.isBlank()) cliError("$key=<option1,option2,...> 缺少参数")
    when (value) {
        "all" -> return onAll()
        "none" -> return
    }
    val values = value.split(",")
    values.forEach {
        if (it !in availableOptions) {
            cliError("$key=$it 不可用的参数")
        }
    }
    values.groupingBy { it }
        .eachCount()
        .filter { it.value > 1 }
        .keys
        .firstOrNull()
        ?.let { cliError("$key=$it 重复的参数") }
    onAction(values.toSet())
}