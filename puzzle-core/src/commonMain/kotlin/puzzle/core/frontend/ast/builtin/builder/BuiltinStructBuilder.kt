package puzzle.core.frontend.ast.builtin.builder

import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.declaration.TopLevelAllowedDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.ast.type.SuperTypeReference
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.ModifierKind

@PzlBuiltinDsl
class BuiltinStructBuilder {
	
	val parameters: List<Parameter>
		field = mutableListOf()
	
	val modifiers: List<Modifier>
		field = mutableListOf()
	
	val primaryCtorModifiers: List<Modifier>
		field = mutableListOf()
	
	val annotationCalls: List<AnnotationCall>
		field = mutableListOf()
	
	val primaryCtorAnnotationCalls: List<AnnotationCall>
		field = mutableListOf()
	
	var typeSpec: TypeSpec? = null
		private set
	
	var contextSpec: DeclarationContextSpec? = null
		private set
	
	val superTypes: List<SuperTypeReference>
		field = mutableListOf()
	
	val members: List<TopLevelAllowedDeclaration>
		field = mutableListOf()
	
	fun parameter(
		name: String,
		type: String,
		isMutable: Boolean = false,
		isNullable: Boolean = false,
		builder: BuiltinParameterBuilder.() -> Unit = {},
	) {
		val builder = BuiltinParameterBuilder().apply(builder)
		val type = getTypeReference(
			type = type,
			isNullable = isNullable,
			typeArguments = builder.typeArguments
		)
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
	
	fun primaryCtorModifier(modifier: ModifierKind) {
		primaryCtorModifiers += getModifier(modifier)
	}
	
	fun primaryCtorModifiers(modifier1: ModifierKind, modifier2: ModifierKind, vararg modifiers: ModifierKind) {
		primaryCtorModifiers += listOf(modifier1, modifier2, *modifiers).map { getModifier(it) }
	}
	
	fun annotationCall(type: String) {
		annotationCalls += getAnnotationCall(type)
	}
	
	fun primaryCtorAnnotationCall(type: String) {
		primaryCtorAnnotationCalls += getAnnotationCall(type)
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
	
	fun superType(
		type: String,
		builder: BuiltinTypeArgumentBuilder.() -> Unit = {},
	) {
		superTypes += SuperTypeReference(
			type = getNamedType(
				type = type,
				typeArguments = BuiltinTypeArgumentBuilder().apply(builder).typeArguments
			),
			location = SourceLocation.Builtin
		)
	}
	
	fun members(builder: BuiltinAstBuilder.() -> Unit) {
		if (this.members.isNotEmpty()) {
			error("members 不可重复配置")
		}
		this.members += BuiltinAstBuilder().apply(builder).declarations
	}
}