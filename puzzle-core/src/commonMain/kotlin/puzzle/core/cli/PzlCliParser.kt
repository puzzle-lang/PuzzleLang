package puzzle.core.cli

import puzzle.core.exception.cliError

fun parseCliOptions(args: List<String>): List<PzlCliOption> {
	return buildList {
		args.forEach { arg ->
			this += when {
				arg.startsWith("--path=") -> {
					if (this.any { it is PathOption }) {
						cliError("重复的选项: --path")
					}
					parsePath(arg)
				}
				
				arg.startsWith("--debug-features=") -> {
					if (this.any { it is DebugFeatureOption }) {
						cliError("重复的选项: --debug-features")
					}
					parseDebugFeatureOption(arg)
				}
				
				arg.startsWith("--infos=") -> {
					if (this.any { it is InfoOption }) {
						cliError("重复的选项: --infos")
					}
					parseReportOption(arg)
				}
				
				else -> cliError("未知选项: $arg")
			}
		}
	}
}

inline fun <reified T : PzlCliOption> List<PzlCliOption>.findOption(): T? {
	return this.find { it is T } as? T
}

private fun parsePath(arg: String): PathOption {
	val path = arg.removePrefix("--path=")
	return PathOption(path)
}

private val availableDebugFeatures = setOf(
	"output-ast-json"
)

private fun parseDebugFeatureOption(arg: String): DebugFeatureOption {
	val featuresString = arg.removePrefix("--debug-features=")
	if (featuresString == "all") return DebugFeatureOption.All
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
	val enableOutputAstJson = "output-ast-json" in features
	return DebugFeatureOption(
		enableOutputAstJson = enableOutputAstJson,
	)
}

private val availableReports = setOf(
	"progress",
	"ignore",
	"file"
)

private fun parseReportOption(arg: String): InfoOption {
	val logInfosString = arg.removePrefix("--infos=")
	if (logInfosString == "all") return InfoOption.All
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
	val enableProgress = "progress" in logInfos
	val enableIgnore = "ignore" in logInfos
	val enableFile = "file" in logInfos
	return InfoOption(
		enableProgress = enableProgress,
		enableIgnore = enableIgnore,
		enableFile = enableFile
	)
}
