package puzzle.builtin.builder

import puzzle.ast.AnnotationCall
import puzzle.ast.Modifier
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.DeclarationContextSpec
import puzzle.ast.parameter.Parameter
import puzzle.ast.parameter.TypeSpec
import puzzle.ast.statement.Statement
import puzzle.ast.type.ErrorsSpec
import puzzle.ast.type.TypeReference
import puzzle.core.location.SourceLocation
import puzzle.token.kinds.ModifierKind

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
	
	var errorsSpec: ErrorsSpec? = null
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
		val builder = BuiltinDeclarationContextReceiverBuilder().apply(builder)
		if (builder.contextReceivers.isEmpty()) {
			error("context 参数不能为空")
		}
		contextSpec = DeclarationContextSpec(
			receivers = builder.contextReceivers,
			isPropagate = isPropagate,
			location = SourceLocation.Builtin
		)
	}
	
	fun errors(builder: BuiltinErrorsTypeBuilder.() -> Unit) {
		if (errorsSpec != null) {
			error("errorsSpec 不可重复配置")
		}
		val builder = BuiltinErrorsTypeBuilder().apply(builder)
		if (builder.errorTypes.isEmpty()) {
			error("errors 参数不能为空")
		}
		errorsSpec = ErrorsSpec(
			errorTypes = builder.errorTypes,
			location = SourceLocation.Builtin
		)
	}
	
	fun body() {
		this.body = emptyList()
	}
}