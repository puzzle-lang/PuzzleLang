package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.DocComment
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.SourceLocation

@Serializable
class InitDeclaration(
	val docComment: DocComment?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Declaration