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

class ReportOption(
	val enableProgress: Boolean,
	val enableIgnore: Boolean,
	val enableFile: Boolean,
) : PzlCliOption {
	
	companion object {
		
		val All = ReportOption(
			enableProgress = true,
			enableIgnore = true,
			enableFile = true,
		)
		
		val None = ReportOption(
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

private var reportOption: ReportOption? = null

context(root: RootContext)
fun getReportOption(): ReportOption {
	if (reportOption == null) {
		reportOption = root.options.findOption() ?: ReportOption.None
	}
	return reportOption!!
}

context(root: RootContext)
inline fun <R> whenEnableDebugFeatureOutputAstJson(block: () -> R): R? {
	return if (getDebugFeatureOption().enableOutputAstJson) block() else null
}

context(root: RootContext)
inline fun <R> whenEnableReportProgress(block: () -> R): R? {
	return if (getReportOption().enableProgress) block() else null
}

context(root: RootContext)
inline fun <R> whenEnableReportIgnore(block: () -> R): R? {
	return if (getReportOption().enableIgnore) block() else null
}

context(root: RootContext)
inline fun <R> whenEnableReportFile(block: () -> R): R? {
	return if (getReportOption().enableFile) block() else null
}