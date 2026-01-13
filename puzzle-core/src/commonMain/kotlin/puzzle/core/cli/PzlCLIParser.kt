package puzzle.core.cli

fun parseCLIOptions(args: List<String>): List<PzlCLIOption> {
	return buildList {
		args.forEach { arg ->
			val option = when {
				arg.startsWith("--path=") -> parsePath(arg)
				arg.startsWith("--debug-features=") -> parseDebugFeatures(arg)
				else -> error("未知选项: $arg")
			}
			if (this.any { it::class == option::class }) {
				error("重复的选项: $arg")
			}
			this += option
		}
	}
}

private fun parsePath(arg: String): PathOption {
	val path = arg.removePrefix("--path=")
	return PathOption(path)
}

private fun parseDebugFeatures(arg: String): DebugFeaturesOption {
	val features = arg.removePrefix("--debug-features=").split(",").map { value ->
		DebugFeature.entries.find { it.value == value } ?: error("未知参数: --debug-features=$value")
	}
	return DebugFeaturesOption(features)
}