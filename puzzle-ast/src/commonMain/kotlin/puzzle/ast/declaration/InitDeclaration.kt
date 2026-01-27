package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.ast.DocComment
import puzzle.ast.statement.Statement
import puzzle.core.location.SourceLocation

@Serializable
class InitDeclaration(
	val docComment: DocComment?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Declaration