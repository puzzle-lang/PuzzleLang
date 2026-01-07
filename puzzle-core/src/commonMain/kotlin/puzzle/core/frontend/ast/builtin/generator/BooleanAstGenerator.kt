package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.builtin.builder.builtinAst
import puzzle.core.frontend.token.kinds.ModifierKind.BUILTIN
import puzzle.core.frontend.token.kinds.ModifierKind.OVERRIDE

fun generateBooleanAst(): AstFile = builtinAst("Boolean") {
	builtinStruct("Boolean") {
		modifier(BUILTIN)
		superType("Comparable") {
			typeArgument("Boolean")
		}
		members {
			builtinFun("!") {
				modifier(BUILTIN)
				returnType("Boolean")
			}
			builtinFun("&") {
				modifier(BUILTIN)
				parameter("other", "Boolean")
				returnType("Boolean")
			}
			builtinFun("|") {
				modifier(BUILTIN)
				parameter("other", "Boolean")
				returnType("Boolean")
			}
			builtinFun("^") {
				modifier(BUILTIN)
				parameter("other", "Boolean")
				returnType("Boolean")
			}
			builtinFun("<=>") {
				modifiers(BUILTIN, OVERRIDE)
				parameter("other", "Boolean")
				returnType("Boolean")
			}
			builtinFun("toString") {
				modifiers(BUILTIN, OVERRIDE)
				parameter("other", "Boolean")
				returnType("Boolean")
			}
			builtinFun("==") {
				modifiers(BUILTIN, OVERRIDE)
				parameter("other", "Any", isNullable = true)
				returnType("Boolean")
			}
			builtinFun("hash") {
				modifiers(BUILTIN, OVERRIDE)
				returnType("Int")
			}
		}
	}
}