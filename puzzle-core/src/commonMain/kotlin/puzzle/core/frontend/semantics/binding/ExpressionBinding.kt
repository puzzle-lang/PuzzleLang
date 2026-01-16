package puzzle.core.frontend.semantics.binding

import puzzle.core.frontend.ast.expression.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.BlockScope
import puzzle.core.frontend.semantics.scope.FileContextScope

context(_: FileContext)
fun List<Expression>.declares(parent: FileContextScope) {
	this.forEach { it.declare(parent) }
}

context(_: FileContext)
fun Expression.declare(parent: FileContextScope) {
	when (this) {
		is AsExpression -> this.declare(parent)
		is BinaryExpression -> this.declare(parent)
		is ElvisExpression -> this.declare(parent)
		is GroupingExpression -> this.declare(parent)
		is IfExpression -> this.declare(parent)
		is InvokeExpression -> this.declare(parent)
		is ReturnExpression -> this.declare(parent)
		is BreakExpression -> this.declare(parent)
		is IsExpression -> this.declare(parent)
		is LambdaExpression -> this.declare(parent)
		is StringLiteral.Template -> this.declare(parent)
		is LoopExpression -> this.declare(parent)
		is MatchPatternExpression -> this.declare(parent)
		is MatchConditionExpression -> this.declare(parent)
		is MemberAccessExpression -> this.declare(parent)
		is MemberReferenceExpression -> this.declare(parent)
		is MultiValueExpression -> this.declare(parent)
		is NonNullAssertionExpression -> this.declare(parent)
		is TernaryExpression -> this.declare(parent)
		is PrefixUnaryExpression -> this.declare(parent)
		is PostfixUnaryExpression -> this.declare(parent)
		is SuperExpression -> TODO()
		is ThisExpression -> TODO()
		is Identifier -> this.declare(parent)
		is ContinueExpression -> TODO()
		is BooleanLiteral -> TODO()
		is CharLiteral -> TODO()
		is NullLiteral -> TODO()
		is NumberLiteral -> TODO()
		is StringLiteral.Text -> TODO()
		is OracleExpression -> TODO()
	}
}

context(_: FileContext)
private fun AsExpression.declare(parent: FileContextScope) {
	this.expression.declare(parent)
}

context(_: FileContext)
private fun BinaryExpression.declare(parent: FileContextScope) {
	this.left.declare(parent)
	this.right.declare(parent)
}

context(_: FileContext)
private fun ElvisExpression.declare(parent: FileContextScope) {
	this.left.declare(parent)
	this.right.declare(parent)
}

context(_: FileContext)
private fun GroupingExpression.declare(parent: FileContextScope) {
	this.expression.declare(parent)
}

context(_: FileContext)
private fun IfExpression.declare(parent: FileContextScope) {
	this.condition.declare(parent)
	val thenScope = BlockScope(parent)
	this.thenBody.declares(thenScope)
	val elseScope = BlockScope(parent)
	this.elseBody.declares(elseScope)
}

context(_: FileContext)
private fun InvokeExpression.declare(parent: FileContextScope) {
	this.callee.declare(parent)
	this.arguments.declares(parent)
}

context(_: FileContext)
private fun ReturnExpression.declare(parent: FileContextScope) {
	this.expression?.declare(parent)
}

context(_: FileContext)
private fun BreakExpression.declare(parent: FileContextScope) {
	this.expression?.declare(parent)
}

context(_: FileContext)
private fun IsExpression.declare(parent: FileContextScope) {
	this.expression.declare(parent)
}

context(file: FileContext)
private fun LambdaExpression.declare(parent: FileContextScope) {
	val scope = BlockScope(parent)
	this.references.declares(scope)
	this.body.declares(scope)
}

context(_: FileContext)
private fun StringLiteral.Template.declare(parent: FileContextScope) {
	this.parts.forEach { part ->
		if (part !is StringLiteral.Template.Part.Expression) return@forEach
		part.expression.declare(parent)
	}
}

context(_: FileContext)
private fun LoopExpression.declare(parent: FileContextScope) {
	val scope = BlockScope(parent)
	this.body.declares(scope)
}

context(_: FileContext)
private fun MatchPatternExpression.declare(parent: FileContextScope) {
	this.subject.declare(parent)
	this.arms.forEach { arm ->
		arm.patterns.forEach { pattern ->
			if (pattern !is ExpressionMatchPattern) return@forEach
			pattern.expression.declare(parent)
		}
		arm.guard?.declare(parent)
		val scope = BlockScope(parent)
		arm.body.declares(scope)
	}
	if (this.elseBody != null) {
		val scope = BlockScope(parent)
		this.elseBody.declares(scope)
	}
}

context(_: FileContext)
private fun MatchConditionExpression.declare(parent: FileContextScope) {
	this.cases.forEach { case ->
		case.condition.declare(parent)
		val scope = BlockScope(parent)
		case.body.declares(scope)
	}
	if (this.elseBody != null) {
		val scope = BlockScope(parent)
		this.elseBody.declares(scope)
	}
}

context(_: FileContext)
private fun MemberAccessExpression.declare(parent: FileContextScope) {
	this.receiver.declare(parent)
}

context(_: FileContext)
private fun MemberReferenceExpression.declare(parent: FileContextScope) {
	this.receiver?.declare(parent)
}

context(_: FileContext)
private fun MultiValueExpression.declare(parent: FileContextScope) {
	this.expressions.declares(parent)
}

context(_: FileContext)
private fun NonNullAssertionExpression.declare(parent: FileContextScope) {
	this.receiver.declare(parent)
}

context(_: FileContext)
private fun TernaryExpression.declare(parent: FileContextScope) {
	this.condition.declare(parent)
	this.thenExpression.declare(parent)
	this.elseExpression.declare(parent)
}

context(_: FileContext)
private fun PrefixUnaryExpression.declare(parent: FileContextScope) {
	this.expression.declare(parent)
}

context(_: FileContext)
private fun PostfixUnaryExpression.declare(parent: FileContextScope) {
	this.expression.declare(parent)
}

context(_: FileContext)
private fun Identifier.declare(parent: FileContextScope) {
	val symbols = parent.lookup(this.value)
}