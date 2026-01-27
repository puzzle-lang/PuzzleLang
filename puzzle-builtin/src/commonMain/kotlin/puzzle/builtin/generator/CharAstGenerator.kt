package puzzle.builtin.generator

import puzzle.core.context.ModuleContext
import puzzle.builtin.builder.builtinAst
import puzzle.token.kinds.ModifierKind.*

context(_: ModuleContext)
fun generateCharAst() = builtinAst("Char") {
	builtinStruct("Char") {
		superType("Comparable") {
			typeArgument("Char")
		}
		members {
			builtinFun("<=>") {
				modifiers(BUILTIN, OVERRIDE)
			}
			builtinFun("+") {
				modifier(BUILTIN)
				parameter("other", "Int")
				returnType("Char")
			}
			builtinFun("-") {
				modifier(BUILTIN)
				parameter("other", "Char")
				returnType("Int")
			}
			builtinFun("-") {
				modifier(BUILTIN)
				parameter("other", "Int")
				returnType("Char")
			}
			builtinFun("++") {
				modifiers(BUILTIN, PREFIX)
				returnType("Char")
			}
			builtinFun("++") {
				modifiers(BUILTIN, POSTFIX)
				returnType("Char")
			}
			builtinFun("--") {
				modifiers(BUILTIN, PREFIX)
				returnType("Char")
			}
			builtinFun("--") {
				modifiers(BUILTIN, POSTFIX)
				returnType("Char")
			}
			builtinFun("toInt") {
				modifier(BUILTIN)
				returnType("Int")
			}
			builtinFun("toString") {
				modifiers(BUILTIN, OVERRIDE)
				returnType("String")
			}
			builtinFun("==") {
				modifiers(BUILTIN, OVERRIDE)
				parameter("other", "Any", isNullable = true)
				returnType("Boolean")
			}
			builtinFun("hashCode") {
				modifiers(BUILTIN, OVERRIDE)
				returnType("Int")
			}
		}
	}
}