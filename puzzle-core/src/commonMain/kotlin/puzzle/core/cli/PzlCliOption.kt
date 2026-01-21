package puzzle.core.cli

object PzlCliOption {
	
	val path = PathOption
	
	val debugFeature = DebugFeatureOption
	
	val info = InfoOption
	
	object PathOption {
		
		var init = false
		
		var path: String? = null
	}
	
	object DebugFeatureOption {
		
		var init = false
		
		var enableOutputAstJson = false
		
		var enableAnsiColor = false
		
		var enableErrorStack = false
	}
	
	object InfoOption {
		
		var init = false
		var enableProgress = false
		
		var enableIgnore = false
		
		var enableFile = false
	}
}

val option = PzlCliOption