package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor

sealed interface ExpressionMatcher {
	
	companion object {
		
		val matchers = arrayOf(
			GroupingExpressionMatcher,
			MemberReferenceExpressionMatcher,
			PrefixUnaryExpressionMatcher,
			LiteralExpressionMatcher,
			BinaryExpressionMatcher,
			ElvisExpressionMatcher,
			OracleExpressionMatcher,
			ReturnExpressionMatcher,
			BreakExpressionMatcher,
			ContinueExpressionMatcher,
			TernaryExpressionMatcher,
			IsExpressionMatcher,
			AsExpressionMatcher,
			IfExpressionMatcher,
			MatchExpressionMatcher,
			LoopExpressionMatcher,
			LambdaExpressionMatcher,
			PostfixExpressionMatcher,
			MultiValueExpressionMatcher,
		)
	}
	
	context(cursor: PzlTokenCursor)
	fun match(left: Expression?): Boolean
}

sealed interface RequirePrefixExpressionParser<out E : Expression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun prefixError(): Nothing
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(left: Expression): E
}

sealed interface NoPrefixExpressionParser<out E : Expression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun prefixError(): Nothing
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(): E
}

sealed interface OptionalPrefixExpressionParser<out E : Expression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(left: Expression?): E
}