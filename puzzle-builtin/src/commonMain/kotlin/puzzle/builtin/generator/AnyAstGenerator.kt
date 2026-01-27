package puzzle.builtin.generator

import puzzle.core.context.ModuleContext
import puzzle.builtin.builder.builtinAst
import puzzle.token.kinds.ModifierKind.BUILTIN

context(_: ModuleContext)
fun generateAnyAst() = builtinAst("Any") {
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