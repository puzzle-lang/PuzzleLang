package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.ast.expression.LambdaExpression
import puzzle.core.frontend.ast.parameter.ParameterReference
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.equalsLine
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseStatements
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SymbolTokenKind.*

context(_: FileContext, cursor: PzlTokenCursor)
fun parseLambdaExpression(): LambdaExpression {
	val containsLabel = cursor.offset(-2).kind == AT
	val start = if (containsLabel) cursor.offset(-3).location else cursor.previous.location
	val label = if (containsLabel) cursor.offset(-3).toIdentifier() else null
	val references = parseLambdaParameterReferences()
	val body = parseStatements()
	val end = cursor.previous.location
	return LambdaExpression(label, references, body, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseLambdaParameterReferences(): List<ParameterReference> {
	return buildList {
		while (!cursor.match(ARROW)) {
			val name = tryParseIdentifier(IdentifierTarget.LAMBDA_PARAMETER_REFERENCE) ?: break
			when {
				cursor.match(COLON) -> {
					val type = parseTypeReference(allowLambda = true)
					this += ParameterReference(name, type)
					if (!cursor.check(ARROW)) {
						cursor.expect(COMMA, "lambda 参数引用列表缺少 ','")
					}
				}
				
				cursor.match(ARROW) -> {
					this += ParameterReference(name)
					break
				}
				
				cursor.match(COMMA) -> {
					this += ParameterReference(name)
					continue
				}
				
				else -> {
					cursor.retreat()
					break
				}
			}
		}
	}
}

context(_: FileContext)
fun PzlTokenCursor.matchLambda(): Boolean {
	return this.match { it.kind == LBRACE && it.equalsLine(this.previous) } ||
			this.matchLabel { it.kind == LBRACE && it.equalsLine(this.previous) }
}