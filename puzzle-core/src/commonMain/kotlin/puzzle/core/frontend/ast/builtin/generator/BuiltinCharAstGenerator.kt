package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.builtin.builder.builtinAst
import puzzle.core.frontend.token.kinds.ModifierKind.*

fun generateBuiltinCharAst(): AstFile = builtinAst("Char") {
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