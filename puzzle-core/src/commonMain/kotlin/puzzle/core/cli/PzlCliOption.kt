package puzzle.core.cli

import puzzle.core.frontend.model.RootContext

sealed interface PzlCliOption

class PathOption(
	val path: String,
) : PzlCliOption

class DebugFeatureOption(
	val enableOutputAstJson: Boolean,
	val enableAnsiColor: Boolean,
	val enableErrorStack: Boolean,
) : PzlCliOption {
	
	companion object {
		
		val All = DebugFeatureOption(
			enableOutputAstJson = true,
			enableAnsiColor = true,
			enableErrorStack = true,
		)
		
		val None = DebugFeatureOption(
			enableOutputAstJson = false,
			enableAnsiColor = false,
			enableErrorStack = false,
		)
	}
}

class InfoOption(
	val enableProgress: Boolean,
	val enableIgnore: Boolean,
	val enableFile: Boolean,
) : PzlCliOption {
	
	companion object {
		
		val All = InfoOption(
			enableProgress = true,
			enableIgnore = true,
			enableFile = true,
		)
		
		val None = InfoOption(
			enableProgress = false,
			enableIgnore = false,
			enableFile = false,
		)
	}
}

private var debugFeatureOption: DebugFeatureOption? = null

context(root: RootContext)
val debugFeature: DebugFeatureOption
	get() = debugFeatureOption ?: (root.options.findOption() ?: DebugFeatureOption.None).also {
		debugFeatureOption = it
	}

private var infoOption: InfoOption? = null

context(root: RootContext)
val info: InfoOption
	get() = infoOption ?: (root.options.findOption() ?: InfoOption.None).also {
		infoOption = it
	}