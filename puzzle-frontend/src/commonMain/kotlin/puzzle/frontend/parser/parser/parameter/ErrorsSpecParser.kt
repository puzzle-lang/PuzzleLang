package puzzle.frontend.parser.parser.parameter

import puzzle.ast.type.ErrorsSpec
import puzzle.ast.type.NamedType
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.token.kinds.BracketKind.End.RBRACKET
import puzzle.token.kinds.BracketKind.Start.LBRACKET
import puzzle.token.kinds.ContextualKind.ERRORS
import puzzle.token.kinds.OperatorKind.BIT_OR

context(_: FileContext, cursor: PzlTokenCursor)
fun parseErrorsSpec(): ErrorsSpec? {
	if (!cursor.match(ERRORS)) return null
	val start = cursor.previous.location
	cursor.expect(LBRACKET, "errors 后缺少 '['")
	val errorTypes = buildList {
		do {
			val type = parseTypeReference(allowNullable = false)
			this += type.type as NamedType
			if (!cursor.check(RBRACKET)) {
				cursor.expect(BIT_OR, "errors 类型列表缺少 '|'")
			}
		} while (!cursor.match(RBRACKET))
	}
	val end = cursor.previous.location
	return ErrorsSpec(
		errorTypes = errorTypes,
		location = start span end
	)
}