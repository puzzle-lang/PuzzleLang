package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.builtin.builder.builtinAst
import puzzle.core.frontend.token.kinds.ModifierKind.BUILTIN

fun generateBuiltinAnyAst(): AstFile = builtinAst("Any") {
	builtinTrait("Any") {
		members {
			builtinFun("==") {
				modifier(BUILTIN)
				parameter("other", "Any", isNullable = true)
				returnType("Boolean")
			}
			builtinFun("hash") {
				modifier(BUILTIN)
				returnType("Int")
			}
			builtinFun("toString") {
				modifier(BUILTIN)
				returnType("String")
			}
		}
	}
}