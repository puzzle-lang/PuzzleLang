package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.declaration.*
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.*
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation

fun generateComparableAst(): AstFile {
	return AstFile(
		name = "Comparable.pzl",
		path = null,
		isBuiltin = true,
		packageDeclaration = PackageDeclaration(
			segments = listOf("puzzle"),
			location = SourceLocation.Builtin
		),
		importDeclarations = emptyList(),
		declarations = listOf(
			FunDeclaration(
				name = MagicFunName(
					kind = MagicKind.COMPARE,
					location = SourceLocation.Builtin
				),
				docComment = null,
				parameters = listOf(
					Parameter(
						name = Identifier(
							name = "other",
							location = SourceLocation.Builtin
						),
						modifiers = emptyList(),
						type = TypeReference(
							type = NamedType(
								segments = listOf("T"),
								location = SourceLocation.Builtin
							),
							isNullable = false,
							location = SourceLocation.Builtin
						),
						annotationCalls = emptyList(),
						quantifier = null,
						defaultExpression = null,
						location = SourceLocation.Builtin,
					)
				),
				modifiers = emptyList(),
				returnSpec = SingleReturnSpec(
					type = TypeReference(
						type = NamedType(
							segments = listOf("Int"),
							location = SourceLocation.Builtin
						),
						isNullable = false,
						location = SourceLocation.Builtin
					)
				),
				extension = null,
				typeSpec = TypeSpec(
					reified = false,
					parameters = listOf(
						TypeParameter(
							name = Identifier(
								name = "T",
								location = SourceLocation.Builtin
							),
							variance = Variance(
								kind = VarianceKind.IN,
								location = SourceLocation.Builtin
							),
							location = SourceLocation.Builtin,
							bounds = emptyList(),
							typeExpansion = null,
							defaultType = null
						)
					),
					location = SourceLocation.Builtin
				),
				contextSpec = null,
				annotationCalls = emptyList(),
				body = emptyList(),
				location = SourceLocation.Builtin
			)
		)
	)
}