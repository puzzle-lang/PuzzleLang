package puzzle.frontend.parser.parser.parameter

import puzzle.ast.parameter.Quantifier
import puzzle.ast.parameter.TypeExpansion
import puzzle.ast.parameter.TypeExpansionKind
import puzzle.ast.parameter.VarargKind
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.token.kinds.OperatorKind.PLUS
import puzzle.token.kinds.OperatorKind.STAR
import puzzle.token.kinds.SymbolKind.PLUS_DOUBLE_DOT
import puzzle.token.kinds.SymbolKind.TRIPLE_DOT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseQuantifier(): Quantifier? {
	return when {
		cursor.match(PLUS_DOUBLE_DOT) -> Quantifier(TypeExpansionKind.NOT_EMPTY, cursor.previous.location)
		cursor.match(TRIPLE_DOT) -> Quantifier(TypeExpansionKind.ALLOW_EMPTY, cursor.previous.location)
		cursor.match(PLUS) -> Quantifier(VarargKind.NOT_EMPTY, cursor.previous.location)
		cursor.match(STAR) -> Quantifier(VarargKind.ALLOW_EMPTY, cursor.previous.location)
		else -> null
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
fun parseTypeExpansion(): TypeExpansion? {
	return when {
		cursor.match(PLUS_DOUBLE_DOT) -> TypeExpansion(TypeExpansionKind.NOT_EMPTY, cursor.previous.location)
		cursor.match(TRIPLE_DOT) -> TypeExpansion(TypeExpansionKind.ALLOW_EMPTY, cursor.previous.location)
		else -> null
	}
}