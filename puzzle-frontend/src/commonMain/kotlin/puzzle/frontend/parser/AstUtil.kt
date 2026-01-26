package puzzle.frontend.parser

import puzzle.ast.expression.Identifier

val Identifier.isAnonymousBinding: Boolean
	get() = this.value == "_"