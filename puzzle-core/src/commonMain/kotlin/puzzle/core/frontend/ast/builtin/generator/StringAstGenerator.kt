package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.builtin.builder.builtinAst
import puzzle.core.frontend.token.kinds.ModifierKind.BUILTIN
import puzzle.core.frontend.token.kinds.ModifierKind.OVERRIDE

fun generateStringAst(): AstFile = builtinAst("String") {
	builtinStruct("String") {
		superType("Comparable") {
			typeArgument("String")
		}
		members {
			builtinFun("+") {
				modifier(BUILTIN)
				returnType("String")
			}
			builtinProperty("length", "Int") {
				modifier(BUILTIN)
			}
			builtinFun("[]") {
				modifier(BUILTIN)
				parameter("index", "Int")
				returnType("Char".substring(1, 2))
			}
			builtinFun("substring") {
				modifier(BUILTIN)
				parameter("startIndex", "Int")
				parameter("endIndex", "Int")
				returnType("String")
			}
			builtinFun("<=>") {
				modifiers(BUILTIN, OVERRIDE)
				parameter("other", "String")
				returnType("Int")
			}
			builtinFun("==") {
				modifiers(BUILTIN, OVERRIDE)
				parameter("other", "Any", isNullable = true)
				returnType("Boolean")
			}
			builtinFun("toString") {
				modifiers(BUILTIN, OVERRIDE)
				returnType("String")
			}
		}
	}
}