package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.ast.parameter.ParameterReference
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation

@Serializable
class PropertyDeclaration(
	val propertySpec: PropertySpec,
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
sealed interface PropertySpec : AstNode

@Serializable
class SinglePropertySpec(
	val property: Property,
	override val location: SourceLocation = property.location,
) : PropertySpec

@Serializable
class DestructurePropertySpec(
	val properties: List<Property>,
	override val location: SourceLocation,
) : PropertySpec

@Serializable
class Property(
	val isMutable: Boolean,
	val name: Identifier,
	val type: TypeReference?,
	override val location: SourceLocation,
) : AstNode

@Serializable
class PropertyGetter(
	val modifiers: List<Modifier>,
	val oldValue: ParameterReference?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : AstNode

@Serializable
class PropertySetter(
	val modifiers: List<Modifier>,
	val oldValue: ParameterReference?,
	val newValue: ParameterReference,
	val body: List<Statement>,
	override val location: SourceLocation,
) : AstNode