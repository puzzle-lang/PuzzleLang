package puzzle.builtin.generator

import puzzle.core.context.ModuleContext
import puzzle.builtin.builder.builtinAst
import puzzle.token.kinds.ModifierKind.*

context(_: ModuleContext)
fun generateNumberAst() = builtinAst("Number") {
	val signedTypes = arrayOf("Byte", "Short", "Int", "Long", "Float", "Double")
	
	builtinTrait("Number") {
		modifier(SEALED)
		members {
			signedTypes.forEach { type ->
				builtinFun("to$type") {
					returnType(type)
				}
			}
		}
	}
	
	val arithmeticOperators = arrayOf("+", "-", "*", "/", "%")
	val shiftOperators = arrayOf("<<", ">>", ">>>")
	val bitwiseLogicOperators = arrayOf("|", "&", "^")
	
	signedTypes.forEach { structType ->
		builtinStruct(structType) {
			superType("Number")
			superType("Comparable") {
				typeArgument(structType)
			}
			members {
				signedTypes.forEach { type ->
					builtinFun("to$type") {
						modifiers(BUILTIN, OVERRIDE)
						returnType(type)
					}
				}
				if (structType == "Int") {
					builtinFun("toChar") {
						modifier(BUILTIN)
						returnType("Char")
					}
				}
				signedTypes.forEach { type ->
					builtinFun("<=>") {
						modifier(BUILTIN)
						if (structType == type) modifier(OVERRIDE)
						parameter("other", type)
						returnType("Int")
					}
				}
				arithmeticOperators.forEach { operator ->
					signedTypes.forEach { type ->
						builtinFun(operator) {
							modifier(BUILTIN)
							parameter("other", type)
							returnType(arithmeticResultType(structType, type))
						}
					}
				}
				builtinFun("~") {
					modifier(BUILTIN)
					returnType(structType)
				}
				if (structType in arrayOf("Byte", "Short", "Int", "Long")) {
					bitwiseLogicOperators.forEach { operator ->
						builtinFun(operator) {
							modifier(BUILTIN)
							parameter("other", structType)
							returnType(structType)
						}
					}
				}
				if (structType == "Int" || structType == "Long") {
					shiftOperators.forEach { operator ->
						builtinFun(operator) {
							modifier(BUILTIN)
							parameter("bitCount", "Int")
							returnType(structType)
						}
					}
					builtinFun("**") {
						modifier(BUILTIN)
						parameter("n", "Int")
						returnType(structType)
					}
				}
				if (structType in arrayOf("Int", "Long", "Float")) {
					builtinFun("**") {
						modifier(BUILTIN)
						parameter("x", "Float")
						returnType("Float")
					}
				}
				if (structType in arrayOf("Int", "Long", "Double")) {
					builtinFun("**") {
						modifier(BUILTIN)
						parameter("x", "Double")
						returnType("Double")
					}
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
				builtinFun("toString") {
					modifiers(BUILTIN, OVERRIDE)
					returnType("String")
				}
			}
		}
	}
}

private fun arithmeticResultType(left: String, right: String): String {
	return when {
		left == "Double" || right == "Double" -> "Float"
		left == "Float" || right == "Float" -> "Float"
		left == "Long" || right == "Long" -> "Long"
		else -> "Int"
	}
}