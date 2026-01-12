package puzzle.core.frontend.parser.parser.parameter

import puzzle.core.frontend.ast.type.ErrorsSpec
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.frontend.token.kinds.ContextualKind.ERRORS
import puzzle.core.frontend.token.kinds.OperatorKind.BIT_OR

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