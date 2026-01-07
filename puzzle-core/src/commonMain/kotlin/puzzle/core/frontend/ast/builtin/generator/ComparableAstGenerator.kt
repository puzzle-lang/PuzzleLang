package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.builtin.builder.builtinAst
import puzzle.core.frontend.ast.parameter.VarianceKind

fun generateComparableAst(): AstFile = builtinAst("Comparable") {
	builtinTrait("Comparable") {
		type {
			typeParameter("T", VarianceKind.IN)
		}
		members {
			builtinFun("<=>") {
				parameter("other", "T")
				returnType("Int")
			}
		}
	}
}