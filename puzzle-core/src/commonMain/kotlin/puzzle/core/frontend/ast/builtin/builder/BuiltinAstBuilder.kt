package puzzle.core.frontend.ast.builtin.builder

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.Symbol
import puzzle.core.frontend.ast.declaration.*
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.SymbolKind
import puzzle.core.util.isIdentifierString

@PzlBuiltinDsl
class BuiltinAstBuilder {
	
	private companion object {
		
		private val magicKindMap = mapOf(
			"[]" to MagicKind.GETTER,
			"[]=" to MagicKind.SETTER,
			"<=>" to MagicKind.COMPARE
		)
	}
	
	val declarations: List<TopLevelAllowedDeclaration>
		field = mutableListOf()
	
	fun builtinFun(
		name: String,
		builder: BuiltinFunBuilder.() -> Unit = {},
	) {
		val funName = when {
			name in magicKindMap -> {
				MagicFunName(
					kind = magicKindMap[name]!!,
					location = SourceLocation.Builtin
				)
			}
			
			SymbolKind.kinds.any { it.value == name } -> {
				SymbolFunName(
					symbol = Symbol(
						kind = SymbolKind.kinds.first { it.value == name },
						location = SourceLocation.Builtin
					)
				)
			}
			
			!name.isIdentifierString() -> error("$name 不是合法的表示符")
			
			else -> IdentifierFunName(
				name = Identifier(
					name = name,
					location = SourceLocation.Builtin
				)
			)
		}
		val builder = BuiltinFunBuilder().apply(builder)
		val returnSpec = when {
			builder.returnTypes.isEmpty() -> null
			builder.returnTypes.size == 1 -> SingleReturnSpec(
				type = builder.returnTypes.first(),
				location = SourceLocation.Builtin
			)
			
			else -> MultiReturnSpec(
				types = builder.returnTypes,
				location = SourceLocation.Builtin
			)
		}
		declarations += FunDeclaration(
			name = funName,
			docComment = null,
			parameters = builder.parameters,
			modifiers = builder.modifiers,
			returnSpec = returnSpec,
			extension = builder.extension,
			typeSpec = builder.typeSpec,
			contextSpec = builder.contextSpec,
			annotationCalls = builder.annotationCalls,
			body = builder.body,
			location = SourceLocation.Builtin,
		)
	}
	
	fun builtinTrait(
		name: String,
		builder: BuiltinTraitBuilder.() -> Unit = {},
	) {
		val builder = BuiltinTraitBuilder().apply(builder)
		declarations += TraitDeclaration(
			name = Identifier(
				name = name,
				location = SourceLocation.Builtin
			),
			docComment = null,
			modifiers = builder.modifiers,
			typeSpec = builder.typeSpec,
			contextSpec = builder.contextSpec,
			annotationCalls = builder.annotationCalls,
			superTypes = builder.superTypes,
			members = builder.members,
			location = SourceLocation.Builtin,
		)
	}
	
	fun builtinStruct(
		name: String,
		builder: BuiltinStructBuilder.() -> Unit,
	) {
		val builder = BuiltinStructBuilder().apply(builder)
		declarations += StructDeclaration(
			name = Identifier(
				name = name,
				location = SourceLocation.Builtin
			),
			docComment = null,
			modifiers = builder.modifiers,
			primaryCtorModifiers = builder.primaryCtorModifiers,
			primaryCtorAnnotationCalls = builder.primaryCtorAnnotationCalls,
			parameters = builder.parameters,
			superTypes = builder.superTypes,
			typeSpec = builder.typeSpec,
			contextSpec = builder.contextSpec,
			annotationCalls = builder.annotationCalls,
			inits = emptyList(),
			members = builder.members,
			location = SourceLocation.Builtin,
		)
	}
	
	fun builtinProperty(
		name: String,
		type: String,
		isNullable: Boolean = false,
		builder: BuiltinPropertyBuilder.() -> Unit = {},
	) {
		val builder = BuiltinPropertyBuilder().apply(builder)
		declarations += PropertyDeclaration(
			name = Identifier(
				name = name,
				location = SourceLocation.Builtin
			),
			type = getTypeReference(
				type = type,
				isNullable = isNullable,
				typeArguments = builder.typeArguments,
			),
			modifiers = builder.modifiers,
			typeSpec = null,
			contextSpec = null,
			annotationCalls = builder.annotationCalls,
			extension = null,
			location = SourceLocation.Builtin
		)
	}
}

fun builtinAst(
	name: String,
	builder: BuiltinAstBuilder.() -> Unit,
): AstFile {
	return AstFile(
		name = name,
		sourcePath = null,
		isBuiltin = true,
		packageDeclaration = PackageDeclaration(
			segments = listOf("puzzle"),
			location = SourceLocation.Builtin,
		),
		importDeclarations = emptyList(),
		declarations = BuiltinAstBuilder().apply(builder).declarations
	)
}