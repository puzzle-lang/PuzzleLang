package puzzle.core.frontend.parser.parser

import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.ArgumentTarget
import puzzle.core.frontend.parser.parser.expression.parseArguments
import puzzle.core.frontend.parser.parser.type.parseNamedType
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.SymbolTokenKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseAnnotationCalls(): List<AnnotationCall> {
	if (!cursor.match(AT)) {
		return emptyList()
	}
	return buildList {
		do {
			this += parseAnnotationCall()
		} while (cursor.match(AT))
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseAnnotationCall(): AnnotationCall {
	val start = cursor.previous.location
	val type = parseNamedType()
	if (!cursor.match(LPAREN)) {
		val location = start span cursor.previous.location
		return AnnotationCall(type, location)
	}
	val arguments = parseArguments(ArgumentTarget.ANNOTATION)
	val location = start span cursor.previous.location
	return AnnotationCall(type, location, arguments)
}