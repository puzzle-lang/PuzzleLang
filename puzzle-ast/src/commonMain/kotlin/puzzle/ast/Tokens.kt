package puzzle.ast

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.location.SourceLocation
import puzzle.token.kinds.AssignmentKind
import puzzle.token.kinds.ModifierKind
import puzzle.token.kinds.OperatorKind
import puzzle.token.kinds.SymbolKind

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