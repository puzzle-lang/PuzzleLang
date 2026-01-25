package puzzle.token.kinds

import puzzle.core.frontend.ast.Modifier

infix fun ModifierKind.isIn(modifiers: List<Modifier>): Boolean {
	return modifiers.any { it.kind == this }
}