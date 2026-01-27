package puzzle.builtin.builder

import puzzle.ast.AnnotationCall
import puzzle.ast.Modifier
import puzzle.ast.declaration.TopLevelAllowedDeclaration
import puzzle.ast.parameter.DeclarationContextSpec
import puzzle.ast.parameter.TypeSpec
import puzzle.ast.type.SuperTypeReference
import puzzle.core.location.SourceLocation
import puzzle.token.kinds.ModifierKind

@PzlBuiltinDsl
class BuiltinTraitBuilder {
	
	val modifiers: List<Modifier>
		field = mutableListOf()
	
	val annotationCalls: List<AnnotationCall>
		field = mutableListOf()
	
	var typeSpec: TypeSpec? = null
		private set
	
	var contextSpec: DeclarationContextSpec? = null
		private set
	
	val superTypes: List<SuperTypeReference>
		field = mutableListOf()
	
	val members: List<TopLevelAllowedDeclaration>
		field = mutableListOf()
	
	fun modifier(modifier: ModifierKind) {
		modifiers += getModifier(modifier)
	}
	
	fun modifiers(modifier1: ModifierKind, modifier2: ModifierKind, vararg modifiers: ModifierKind) {
		this.modifiers += listOf(modifier1, modifier2, *modifiers).map { getModifier(it) }
	}
	
	fun annotationCall(type: String) {
		annotationCalls += getAnnotationCall(type)
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