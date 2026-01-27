package puzzle.builtin.generator

import puzzle.ast.parameter.VarianceKind
import puzzle.core.context.ModuleContext
import puzzle.builtin.builder.builtinAst

context(_: ModuleContext)
fun generateComparableAst() = builtinAst("Comparable") {
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