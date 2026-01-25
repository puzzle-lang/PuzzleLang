package puzzle.ast.declaration

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.ast.AnnotationCall
import puzzle.ast.DocComment
import puzzle.ast.Modifier
import puzzle.ast.PzlAstNode
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.DeclarationContextSpec
import puzzle.ast.parameter.Parameter
import puzzle.ast.parameter.TypeSpec
import puzzle.ast.statement.Statement
import puzzle.ast.type.ErrorsSpec
import puzzle.ast.type.TypeReference
import puzzle.diagnostic.source.SourceLocation
import puzzle.token.kinds.SymbolKind

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
	val errorsSpec: ErrorsSpec?,
	val annotationCalls: List<AnnotationCall>,
	val body: List<Statement>?,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration

@Serializable
sealed interface ReturnSpec : PzlAstNode

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
sealed interface FunName {
	
	val name: Identifier
}

@Serializable
class IdentifierFunName(
	override val name: Identifier,
) : FunName

@Serializable
class SymbolFunName(
	override val name: Identifier,
	@Contextual
	val kind: SymbolKind,
) : FunName

@Serializable
class MagicFunName(
	override val name: Identifier,
	val kind: MagicKind,
) : FunName

@Serializable
enum class MagicKind(
	val value: String,
) {
	GETTER("[]"),
	SETTER("[]="),
	COMPARE("<=>")
}