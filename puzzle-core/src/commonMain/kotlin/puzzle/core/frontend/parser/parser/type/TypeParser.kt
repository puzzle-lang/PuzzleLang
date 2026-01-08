package puzzle.core.frontend.parser.parser.type

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.parser.argument.parseTypeArguments
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.tryParseIdentifierString
import puzzle.core.frontend.token.kinds.AccessKind.DOT
import puzzle.core.frontend.token.kinds.ContextualKind.WITH
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA

context(_: FileContext, cursor: PzlTokenCursor)
fun parseNamedType(): NamedType {
	val start = cursor.current.location
	val segments = buildList {
		do {
			val segment = tryParseIdentifierString(IdentifierTarget.TYPE_REFERENCE)
			if (segment != null) {
				this += segment
			} else {
				if (this.isEmpty()) {
					syntaxError("类型识别错误", cursor.current)
				}
				cursor.retreat()
				break
			}
		} while (cursor.match(DOT))
	}
	val typeArguments = parseTypeArguments()
	val location = start span cursor.previous.location
	return NamedType(segments, location, typeArguments)
}

context(_: FileContext, cursor: PzlTokenCursor)
fun parseWithTypes(): List<NamedType> {
	if (!cursor.match(WITH)) return emptyList()
	return buildList {
		do {
			this += parseNamedType()
		} while (cursor.match(COMMA))
	}
}