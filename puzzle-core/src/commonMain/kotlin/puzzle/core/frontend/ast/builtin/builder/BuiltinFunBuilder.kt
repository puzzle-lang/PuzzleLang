package puzzle.core.frontend.ast.builtin.builder

import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.ModifierKind

@PzlBuiltinDsl
class BuiltinFunBuilder {
	
	val parameters: List<Parameter>
		field = mutableListOf()
	
	val modifiers: List<Modifier>
		field = mutableListOf()
	
	val annotationCalls: List<AnnotationCall>
		field = mutableListOf()
	
	val returnTypes: List<TypeReference>
		field = mutableListOf()
	
	var extension: TypeReference? = null
		private set
	
	var typeSpec: TypeSpec? = null
		private set
	
	var contextSpec: DeclarationContextSpec? = null
		private set
	
	var body: List<Statement>? = null
		private set
	
	fun parameter(
		name: String,
		type: String,
		isMutable: Boolean = false,
		isNullable: Boolean = false,
		builder: BuiltinParameterBuilder.() -> Unit = {},
	) {
		val builder = BuiltinParameterBuilder().apply(builder)
		val type = getTypeReference(type, isNullable, builder.typeArguments)
		parameters += Parameter(
			name = Identifier(
				value = name,
				location = SourceLocation.Builtin,
			),
			isMutable = isMutable,
			modifiers = builder.modifiers,
			type = type,
			annotationCalls = builder.annotationCalls,
			quantifier = builder.quantifier,
			defaultExpression = null,
			location = SourceLocation.Builtin
		)
	}
	
	fun modifier(modifier: ModifierKind) {
		modifiers += getModifier(modifier)
	}
	
	fun modifiers(modifier1: ModifierKind, modifier2: ModifierKind, vararg modifiers: ModifierKind) {
		this.modifiers += listOf(modifier1, modifier2, *modifiers).map { getModifier(it) }
	}
	
	fun annotationCall(type: String) {
		annotationCalls += getAnnotationCall(type)
	}
	
	fun returnType(
		type: String,
		isNullable: Boolean = false,
		builder: BuiltinTypeArgumentBuilder.() -> Unit = {},
	) {
		returnTypes += getTypeReference(
			type = type,
			isNullable = isNullable,
			typeArguments = BuiltinTypeArgumentBuilder().apply(builder).typeArguments
		)
	}
	
	fun extension(
		type: String,
		isNullable: Boolean = false,
		builder: BuiltinTypeArgumentBuilder.() -> Unit = {},
	) {
		if (extension != null) {
			error("extension 不可重复配置")
		}
		extension = getTypeReference(
			type = type,
			isNullable = isNullable,
			typeArguments = BuiltinTypeArgumentBuilder().apply(builder).typeArguments
		)
	}
	
	fun type(
		reified: Boolean = false,
		builder: BuiltinTypeParameterBuilder.() -> Unit,
	) {
		if (typeSpec != null) {
			error("typeSpec 不可重复配置")
		}
		typeSpec = TypeSpec(
			reified = reified,
			parameters = BuiltinTypeParameterBuilder().apply(builder).typeParameters,
			location = SourceLocation.Builtin
		)
	}
	
	fun context(
		isPropagate: Boolean = true,
		builder: BuiltinDeclarationContextReceiverBuilder.() -> Unit,
	) {
		if (contextSpec != null) {
			error("contextSpec 不可重复配置")
		}
		contextSpec = DeclarationContextSpec(
			receivers = BuiltinDeclarationContextReceiverBuilder().apply(builder).contextReceivers,
			isPropagate = isPropagate,
			location = SourceLocation.Builtin
		)
	}
	
	fun body() {
		this.body = emptyList()
	}
}