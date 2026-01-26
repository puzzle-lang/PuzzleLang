package puzzle.sema.symbol

import puzzle.ast.Modifier
import puzzle.token.kinds.Visibility

val List<Modifier>.visibility: Visibility?
	get() = this.firstNotNullOfOrNull { modifier ->
		modifier.kind as? Visibility
	}