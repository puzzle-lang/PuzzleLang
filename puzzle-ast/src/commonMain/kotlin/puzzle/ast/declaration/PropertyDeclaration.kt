package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.ast.AnnotationCall
import puzzle.ast.PzlAstNode
import puzzle.ast.Modifier
import puzzle.ast.expression.Expression
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.DeclarationContextSpec
import puzzle.ast.parameter.ParameterReference
import puzzle.ast.parameter.TypeSpec
import puzzle.ast.statement.Statement
import puzzle.ast.type.TypeReference
import puzzle.core.location.SourceLocation

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
sealed interface PropertySpec : PzlAstNode

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
) : PzlAstNode

@Serializable
class PropertyGetter(
	val modifiers: List<Modifier>,
	val oldValue: ParameterReference?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
class PropertySetter(
	val modifiers: List<Modifier>,
	val oldValue: ParameterReference?,
	val newValue: ParameterReference,
	val body: List<Statement>,
	override val location: SourceLocation,
) : PzlAstNode