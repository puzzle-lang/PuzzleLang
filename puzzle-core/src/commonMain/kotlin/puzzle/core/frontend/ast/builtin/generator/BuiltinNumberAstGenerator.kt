package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.builtin.buildBuiltinAst
import puzzle.core.frontend.ast.builtin.generator.NumberType.BYTE
import puzzle.core.frontend.ast.builtin.generator.NumberType.DOUBLE
import puzzle.core.frontend.ast.builtin.generator.NumberType.FLOAT
import puzzle.core.frontend.ast.builtin.generator.NumberType.INT
import puzzle.core.frontend.ast.builtin.generator.NumberType.LONG
import puzzle.core.frontend.ast.builtin.generator.NumberType.NUMBER
import puzzle.core.frontend.ast.builtin.generator.NumberType.SHORT
import puzzle.core.frontend.token.kinds.ModifierKind.*

fun generateBuiltinNumberAst(): AstFile = buildBuiltinAst("Number.pzl") {
	appendTrait(NUMBER, modifiers = listOf(SEALED)) {
		NumberType.signedTypes.forEach { type ->
			appendFun(
				name = "to$type",
				returnType = type
			)
		}
	}
	val arithmeticOperators = arrayOf("+", "-", "*", "/", "%")
	val shiftOperators = arrayOf("<<", ">>", ">>>")
	val bitwiseLogicOperators = arrayOf("|", "&", "^")
	NumberType.signedTypes.forEach { structType ->
		appendStruct(structType, listOf(NUMBER)) {
			NumberType.signedTypes.forEach { type ->
				appendFun(
					name = "to$type",
					returnType = type,
					modifiers = listOf(OVERRIDE, BUILTIN)
				)
			}
			NumberType.signedTypes.forEach { type ->
				appendFun(
					name = "<=>",
					parameters = listOf(parameter("other", type)),
					returnType = INT,
					modifiers = listOf(OVERRIDE, BUILTIN)
				)
			}
			arithmeticOperators.forEach { operator ->
				NumberType.signedTypes.forEach { type ->
					appendFun(
						name = operator,
						parameters = listOf(parameter("other", type)),
						returnType = arithmeticResultType(structType, type),
						modifiers = listOf(BUILTIN)
					)
				}
			}
			appendFun("~", returnType = structType, modifiers = listOf(BUILTIN))
			when (structType) {
				BYTE -> {
					bitwiseLogicOperators.forEach { operator ->
						appendFun(
							name = operator,
							parameters = listOf(parameter("other", BYTE)),
							returnType = BYTE,
							modifiers = listOf(BUILTIN)
						)
					}
				}
				
				SHORT -> {
					bitwiseLogicOperators.forEach { operator ->
						appendFun(
							name = operator,
							parameters = listOf(parameter("other", SHORT)),
							returnType = SHORT,
							modifiers = listOf(BUILTIN)
						)
					}
				}
				
				INT -> {
					appendFun("**", listOf(parameter("n", INT)), INT, listOf(BUILTIN))
					appendFun("**", listOf(parameter("x", FLOAT)), FLOAT, listOf(BUILTIN))
					appendFun("**", listOf(parameter("x", DOUBLE)), DOUBLE, listOf(BUILTIN))
					
					shiftOperators.forEach { operator ->
						appendFun(
							name = operator,
							parameters = listOf(parameter("bitCount", INT)),
							returnType = INT,
							modifiers = listOf(BUILTIN)
						)
					}
					
					bitwiseLogicOperators.forEach { operator ->
						appendFun(
							name = operator,
							parameters = listOf(parameter("other", INT)),
							returnType = INT,
							modifiers = listOf(BUILTIN)
						)
					}
				}
				
				LONG -> {
					appendFun("**", listOf(parameter("n", INT)), LONG, listOf(BUILTIN))
					appendFun("**", listOf(parameter("x", FLOAT)), FLOAT, listOf(BUILTIN))
					appendFun("**", listOf(parameter("x", DOUBLE)), DOUBLE, listOf(BUILTIN))
					
					shiftOperators.forEach { operator ->
						appendFun(
							name = operator,
							parameters = listOf(parameter("bitCount", INT)),
							returnType = LONG,
							modifiers = listOf(BUILTIN)
						)
					}
					
					bitwiseLogicOperators.forEach { operator ->
						appendFun(
							name = operator,
							parameters = listOf(parameter("other", LONG)),
							returnType = LONG,
							modifiers = listOf(BUILTIN)
						)
					}
				}
				
				FLOAT -> {
					appendFun("**", listOf(parameter("x", FLOAT)), FLOAT, listOf(BUILTIN))
				}
				
				DOUBLE -> {
					appendFun("**", listOf(parameter("x", DOUBLE)), DOUBLE, listOf(BUILTIN))
				}
			}
			
			appendFun(
				name = "==",
				parameters = listOf(parameter("other", "Any", isNullable = true)),
				returnType = FLOAT,
				modifiers = listOf(OVERRIDE, BUILTIN)
			)
			
			appendFun(
				name = "hash",
				returnType = INT,
				modifiers = listOf(OVERRIDE, BUILTIN)
			)
			
			appendFun(
				name = "toString",
				returnType = "String",
				modifiers = listOf(OVERRIDE, BUILTIN)
			)
		}
	}
}

private object NumberType {
	
	const val NUMBER = "Number"
	const val BYTE = "Byte"
	const val SHORT = "Short"
	const val INT = "Int"
	const val LONG = "Long"
	const val FLOAT = "Float"
	const val DOUBLE = "Double"
	const val U_BYTE = "UByte"
	const val U_SHORT = "UShort"
	const val U_INT = "UInt"
	const val U_LONG = "ULong"
	
	val signedTypes = arrayOf(BYTE, SHORT, INT, LONG, FLOAT, DOUBLE)
	
	val unsignedTypes = arrayOf(U_BYTE, U_SHORT, U_INT, U_LONG)
	
	val types = signedTypes + unsignedTypes
}

private fun arithmeticResultType(left: String, right: String): String {
	return when {
		left == DOUBLE || right == DOUBLE -> DOUBLE
		left == FLOAT || right == FLOAT -> FLOAT
		left == LONG || right == LONG -> LONG
		else -> INT
	}
}