package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation

@Serializable
class PropertyDeclaration(
	val name: Identifier,
	val type: TypeReference?,
	val modifiers: List<Modifier>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val extension: TypeReference?,
	override val location: SourceLocation,
	val initializer: Expression? = null,
	val getter: PropertyGetter? = null,
	val setter: PropertySetter? = null,
) : TopLevelAllowedDeclaration

@Serializable
class PropertyGetter(
	val oldValue: Identifier?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : AstNode

@Serializable
class PropertySetter(
	val oldValue: Identifier?,
	val newValue: Identifier,
	val body: List<Statement>,
	override val location: SourceLocation,
) : AstNode