package puzzle.frontend.parser.parser

import puzzle.ast.AnnotationCall
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.ArgumentTarget
import puzzle.frontend.parser.parser.expression.parseArguments
import puzzle.frontend.parser.parser.type.parseNamedType
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.SymbolKind.AT

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