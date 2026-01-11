package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.token.kinds.ModifierKind.*

enum class Visibility {
	PRIVATE,
	FILE,
	PROTECTED,
	INTERNAL,
	MODULE,
	PUBLIC
}

val List<Modifier>.visibility: Visibility?
	get() = this.firstNotNullOfOrNull { modifier ->
		when (modifier.kind) {
			PRIVATE -> Visibility.PRIVATE
			PROTECTED -> Visibility.PROTECTED
			FILE -> Visibility.FILE
			INTERNAL -> Visibility.INTERNAL
			MODULE -> Visibility.MODULE
			PUBLIC -> Visibility.PUBLIC
			else -> null
		}
	}