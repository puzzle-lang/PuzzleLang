package puzzle.core.frontend.parser

import puzzle.core.frontend.ast.expression.Identifier

val Identifier.isAnonymousBinding: Boolean
	get() = this.value == "_"