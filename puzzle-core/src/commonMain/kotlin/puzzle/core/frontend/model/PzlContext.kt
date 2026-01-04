package puzzle.core.frontend.model

import puzzle.core.util.PathWrapper

class PzlContext(
	val sourcePath: PathWrapper,
	var lineStarts: IntArray,
)