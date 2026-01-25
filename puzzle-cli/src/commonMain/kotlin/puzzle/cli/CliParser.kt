package puzzle.cli

import puzzle.base.environment.PzlEnvironment

private val availableArgMap = mapOf(
	"--path" to ::parsePathOption,
	"--debug-features" to ::parseDebugFeatureOption,
	"--infos" to ::parseInfoOption
)

fun parseCliOptions(options: List<String>) {
	val usedArgTypes = mutableSetOf<String>()
	options.forEach { arg ->
		availableArgMap.forEach { (argType, parseOption) ->
			if (arg.startsWith("$argType=")) {
				if (argType in usedArgTypes) {
					cliError("重复的选项: $argType")
				}
				usedArgTypes += arg
				val value = arg.removePrefix("$argType=")
				parseOption(value)
			}
		}
		cliError("未知选项: $arg")
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

private val availableDebugFeatures = setOf(
	"output-ast-json",
	"ansi-color",
	"error-stack"
)

private fun parseDebugFeatureOption(value: String) {
	if (value.isBlank()) cliError("--debug-features=<option1,option2,...> 缺少参数")
	when (value) {
		"all" -> {
			PzlEnvironment.enableOutputAstJson = true
			PzlEnvironment.enableAnsiColor = true
			PzlEnvironment.enableErrorStack = true
			return
		}
		
		"none" -> return
	}
	val values = value.split(",")
	values.forEach {
		if (it !in availableDebugFeatures) {
			cliError("--debug-features=$it 不可用的参数")
		}
	}
	values.groupingBy { it }
		.eachCount()
		.filter { it.value > 1 }
		.keys
		.firstOrNull()
		?.let { cliError("--debug-features=$it 重复的参数") }
	PzlEnvironment.enableOutputAstJson = "output-ast-json" in values
	PzlEnvironment.enableAnsiColor = "ansi-color" in values
	PzlEnvironment.enableErrorStack = "error-stack" in values
}

private val availableReports = setOf(
	"progress",
	"ignore",
	"file"
)

private fun parseInfoOption(value: String) {
	if (value.isBlank()) cliError("--infos=<option1,option2,...> 缺少参数")
	when (value) {
		"all" -> {
			PzlEnvironment.enableInfoProgress = true
			PzlEnvironment.enableInfoIgnore = true
			PzlEnvironment.enableInfoFile = true
			return
		}
		
		"none" -> return
	}
	val values = value.split(",")
	values.forEach {
		if (it !in availableReports) {
			cliError("--infos=$it 不可用的参数")
		}
	}
	values.groupingBy { it }
		.eachCount()
		.filter { it.value > 1 }
		.keys
		.firstOrNull()
		?.let { cliError("--infos=$it 重复的参数") }
	PzlEnvironment.enableInfoProgress = "progress" in values
	PzlEnvironment.enableInfoIgnore = "ignore" in values
	PzlEnvironment.enableInfoFile = "file" in values
}