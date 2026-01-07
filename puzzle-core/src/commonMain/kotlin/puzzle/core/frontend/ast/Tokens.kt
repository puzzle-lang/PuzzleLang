package puzzle.core.frontend.ast

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.AssignmentKind
import puzzle.core.frontend.token.kinds.ModifierKind
import puzzle.core.frontend.token.kinds.OperatorKind
import puzzle.core.frontend.token.kinds.SymbolTokenKind

@Serializable
class SymbolToken(
	@Contextual
	val kind: SymbolTokenKind,
	override val location: SourceLocation,
) : AstNode

@Serializable
class Modifier(
	@Contextual
	val kind: ModifierKind,
	override val location: SourceLocation,
) : AstNode

@Serializable
class Operator(
	@Contextual
	val kind: OperatorKind,
	override val location: SourceLocation,
) : AstNode

@Serializable
class Assignment(
	@Contextual
	val kind: AssignmentKind,
	override val location: SourceLocation,
) : AstNode