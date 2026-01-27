package puzzle.frontend.util

import puzzle.ast.Modifier
import puzzle.token.kinds.ModifierKind

infix fun ModifierKind.isIn(modifiers: List<Modifier>): Boolean {
	return modifiers.any { it.kind == this }
}