package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.ast.expression.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.BooleanKind.FALSE
import puzzle.core.frontend.token.kinds.BooleanKind.TRUE
import puzzle.core.frontend.token.kinds.CharKind
import puzzle.core.frontend.token.kinds.LiteralKind
import puzzle.core.frontend.token.kinds.LiteralKind.NULL
import puzzle.core.frontend.token.kinds.NumberKind
import puzzle.core.frontend.token.kinds.StringKind

context(_: FileContext, cursor: PzlTokenCursor)
fun parseLiteralExpression(): LiteralExpression {
	val token = cursor.previous
	return when (val kind = token.kind as LiteralKind) {
		FALSE -> BooleanLiteral(false, token.location)
		TRUE -> BooleanLiteral(true, token.location)
		NULL -> NullLiteral(token.location)
		is CharKind -> CharLiteral(kind.value, token.location)
		is NumberKind -> NumberLiteral(kind.value, kind.system, kind.type, token.location)
		is StringKind.Text -> StringLiteral.Text(kind.value, token.location)
		is StringKind.Template -> {
			val parts = kind.parts.map {
				when (it) {
					is StringKind.Template.Part.Expression -> {
						val expression = context(PzlTokenCursor(it.tokens)) {
							parseExpressionChain()
						}
						StringLiteral.Template.Part.Expression(expression, it.location)
					}
					
					is StringKind.Template.Part.Text -> StringLiteral.Template.Part.Text(it.value, it.location)
				}
			}
			StringLiteral.Template(parts, token.location)
		}
	}
}