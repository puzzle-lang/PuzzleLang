package puzzle.core.frontend.ast

import puzzle.core.frontend.model.SourceLocation

interface PzlAstNode {
	
	val location: SourceLocation
}