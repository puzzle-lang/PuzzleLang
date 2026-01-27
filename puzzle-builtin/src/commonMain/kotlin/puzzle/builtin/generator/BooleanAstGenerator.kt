package puzzle.builtin.generator

import puzzle.core.context.ModuleContext
import puzzle.builtin.builder.builtinAst
import puzzle.token.kinds.ModifierKind.BUILTIN
import puzzle.token.kinds.ModifierKind.OVERRIDE

context(_: ModuleContext)
fun generateBooleanAst() = builtinAst("Boolean") {
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