package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.*
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation

@Serializable
class FunDeclaration(
	val name: FunName,
	val docComment: DocComment?,
	val parameters: List<Parameter>,
	val modifiers: List<Modifier>,
	val returnSpec: ReturnSpec?,
	val extension: TypeReference?,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val body: List<Statement>?,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration

@Serializable
sealed interface ReturnSpec : AstNode

@Serializable
class SingleReturnSpec(
	val type: TypeReference,
	override val location: SourceLocation = type.location,
) : ReturnSpec

@Serializable
class MultiReturnSpec(
	val types: List<TypeReference>,
	override val location: SourceLocation,
) : ReturnSpec

@Serializable
sealed interface FunName : AstNode

@Serializable
class IdentifierFunName(
	val name: Identifier,
	override val location: SourceLocation = name.location,
) : FunName

@Serializable
class SymbolFunName(
	val symbol: SymbolToken,
	override val location: SourceLocation = symbol.location,
) : FunName

@Serializable
class MagicFunName(
	val kind: MagicKind,
	override val location: SourceLocation,
) : FunName

@Serializable
enum class MagicKind(
	val value: String,
) {
	GETTER("[]"),
	SETTER("[]="),
	COMPARE("<=>")
}