package puzzle.core.frontend.parser.parser.parameter

import puzzle.core.frontend.ast.parameter.Quantifier
import puzzle.core.frontend.ast.parameter.TypeExpansion
import puzzle.core.frontend.ast.parameter.TypeExpansionKind
import puzzle.core.frontend.ast.parameter.VarargKind
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.OperatorKind.PLUS
import puzzle.core.frontend.token.kinds.OperatorKind.STAR
import puzzle.core.frontend.token.kinds.SymbolTokenKind.PLUS_DOUBLE_DOT
import puzzle.core.frontend.token.kinds.SymbolTokenKind.TRIPLE_DOT

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