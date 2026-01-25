package puzzle.ast

import puzzle.diagnostic.source.SourceLocation

interface PzlAstNode {
	
	val location: SourceLocation
}