package puzzle.token

import puzzle.base.location.SourceLocation
import puzzle.token.kinds.PzlTokenKind

class PzlToken(
	val kind: PzlTokenKind,
	val location: SourceLocation,
) {
	val value: String
		get() = this.kind.value
}