package puzzle.core.frontend.ast

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.AssignmentKind
import puzzle.core.frontend.token.kinds.ModifierKind
import puzzle.core.frontend.token.kinds.OperatorKind
import puzzle.core.frontend.token.kinds.SymbolKind

@Serializable
class SymbolToken(
	@Contextual
	val kind: SymbolKind,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
class Modifier(
	@Contextual
	val kind: ModifierKind,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
class Operator(
	@Contextual
	val kind: OperatorKind,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
class Assignment(
	@Contextual
	val kind: AssignmentKind,
	override val location: SourceLocation,
) : PzlAstNode