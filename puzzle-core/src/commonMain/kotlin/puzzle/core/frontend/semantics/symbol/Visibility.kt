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

fun List<Modifier>.getVisibility(): Visibility {
	this.forEach {
		when (it.kind) {
			PRIVATE -> return Visibility.PUBLIC
			PROTECTED -> return Visibility.PROTECTED
			FILE -> return Visibility.FILE
			INTERNAL -> return Visibility.INTERNAL
			MODULE -> return Visibility.MODULE
			PUBLIC -> return Visibility.PUBLIC
			else -> {}
		}
	}
	return Visibility.PUBLIC
}