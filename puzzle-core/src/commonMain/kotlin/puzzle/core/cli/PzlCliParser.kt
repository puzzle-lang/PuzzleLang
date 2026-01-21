package puzzle.core.cli

import puzzle.core.exception.cliError

fun parseCliOptions(args: List<String>) {
	args.forEach { arg ->
		when {
			arg.startsWith("--path=") -> {
				if (option.path.init) {
					cliError("重复的选项: --path")
				}
				parsePathOption(arg)
				option.path.init = true
			}
			
			arg.startsWith("--debug-features=") -> {
				if (option.debugFeature.init) {
					cliError("重复的选项: --debug-features")
				}
				parseDebugFeatureOption(arg)
				option.debugFeature.init = true
			}
			
			arg.startsWith("--infos=") -> {
				if (option.info.init) {
					cliError("重复的选项: --infos")
				}
				parseInfoOption(arg)
				option.info.init = true
			}
			
			else -> cliError("未知选项: $arg")
		}
	}
}

private fun parsePathOption(arg: String) {
	val path = arg.removePrefix("--path=")
	option.path.path = path
}

private val availableDebugFeatures = setOf(
	"output-ast-json",
	"ansi-color",
	"error-stack"
)

private fun parseDebugFeatureOption(arg: String) {
	val featuresString = arg.removePrefix("--debug-features=")
	when (featuresString) {
		"all" -> {
			option.debugFeature.enableOutputAstJson = true
			option.debugFeature.enableAnsiColor = true
			option.debugFeature.enableErrorStack = true
			return
		}
		
		"none" -> return
	}
	if (featuresString.isBlank()) cliError("--debug-features=<option1,option2,...> 缺少参数")
	val features = featuresString.split(",")
	features.forEach { feature ->
		if (feature !in availableDebugFeatures) {
			cliError("--debug-features=$feature 不可用的参数")
		}
	}
	features.groupingBy { it }
		.eachCount()
		.filter { it.value > 1 }
		.keys
		.firstOrNull()
		?.let { cliError("--debug-features=$it 重复的参数") }
	option.debugFeature.enableOutputAstJson = "output-ast-json" in features
	option.debugFeature.enableAnsiColor = "ansi-color" in features
	option.debugFeature.enableErrorStack = "error-stack" in features
}

private val availableReports = setOf(
	"progress",
	"ignore",
	"file"
)

private fun parseInfoOption(arg: String) {
	val logInfosString = arg.removePrefix("--infos=")
	when (logInfosString) {
		"all" -> {
			option.info.enableProgress = true
			option.info.enableIgnore = true
			option.info.enableFile = true
			return
		}
		
		"none" -> return
	}
	if (logInfosString.isBlank()) cliError("--infos=<option1,option2,...> 缺少参数")
	val logInfos = logInfosString.split(",")
	logInfos.forEach { info ->
		if (info !in availableReports) {
			cliError("--infos=$info 不可用的参数")
		}
	}
	logInfos.groupingBy { it }
		.eachCount()
		.filter { it.value > 1 }
		.keys
		.firstOrNull()
		?.let { cliError("--infos=$it 重复的参数") }
	option.info.enableProgress = "progress" in logInfos
	option.info.enableIgnore = "ignore" in logInfos
	option.info.enableFile = "file" in logInfos
}
