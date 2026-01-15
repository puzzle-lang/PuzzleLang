package puzzle.core.cli

import puzzle.core.frontend.model.RootContext

sealed interface PzlCliOption

class PathOption(
	val path: String,
) : PzlCliOption

class DebugFeatureOption(
	val enableOutputAstJson: Boolean,
) : PzlCliOption {
	
	companion object {
		
		val All = DebugFeatureOption(
			enableOutputAstJson = true,
		)
		
		val None = DebugFeatureOption(
			enableOutputAstJson = false,
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
fun getDebugFeatureOption(): DebugFeatureOption {
	if (debugFeatureOption == null) {
		debugFeatureOption = root.options.findOption() ?: DebugFeatureOption.None
	}
	return debugFeatureOption!!
}

private var infoOption: InfoOption? = null

context(root: RootContext)
fun getInfoOption(): InfoOption {
	if (infoOption == null) {
		infoOption = root.options.findOption() ?: InfoOption.None
	}
	return infoOption!!
}

context(root: RootContext)
inline fun <R> whenEnableDebugFeatureOutputAstJson(block: () -> R): R? {
	return if (getDebugFeatureOption().enableOutputAstJson) block() else null
}

context(root: RootContext)
inline fun <R> whenEnableInfoProgress(block: () -> R): R? {
	return if (getInfoOption().enableProgress) block() else null
}

context(root: RootContext)
inline fun <R> whenEnableInfoIgnore(block: () -> R): R? {
	return if (getInfoOption().enableIgnore) block() else null
}

context(root: RootContext)
inline fun <R> whenEnableInfoFile(block: () -> R): R? {
	return if (getInfoOption().enableFile) block() else null
}