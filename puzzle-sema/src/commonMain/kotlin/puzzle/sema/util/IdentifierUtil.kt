package puzzle.sema.util

import puzzle.ast.expression.Identifier

private const val ANONYMOUS = "_"

val Identifier.isAnonymous: Boolean
	get() = this.value == ANONYMOUS