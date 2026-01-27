package puzzle.cli

import puzzle.core.environment.PzlEnvironment

private val availableArgParseMap = mapOf(
	"--path" to ::parsePathOption,
	"--features" to ::parseFeaturesOption,
	"--exports" to ::parseExportsOption,
	"--infos" to ::parseInfosOption
)

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
}

private fun checkOption() {
	if (PzlEnvironment.projectPath == null) {
		cliError("缺少 --path 选项")
	}
}

private fun parsePathOption(value: String) {
	PzlEnvironment.projectPath = value
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
			PzlEnvironment.enableAnsiColor = true
			PzlEnvironment.enableErrorStack = true
		},
		onAction = { options ->
			PzlEnvironment.enableAnsiColor = "ansi-color" in options
			PzlEnvironment.enableErrorStack = "error-stack" in options
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
			PzlEnvironment.enableExportAst = true
		},
		onAction = { options ->
			PzlEnvironment.enableExportAst = "ast" in options
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
			PzlEnvironment.enableInfoProgress = true
			PzlEnvironment.enableInfoIgnore = true
			PzlEnvironment.enableInfoFile = true
		},
		onAction = { options ->
			PzlEnvironment.enableInfoProgress = "progress" in options
			PzlEnvironment.enableInfoIgnore = "ignore" in options
			PzlEnvironment.enableInfoFile = "file" in options
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