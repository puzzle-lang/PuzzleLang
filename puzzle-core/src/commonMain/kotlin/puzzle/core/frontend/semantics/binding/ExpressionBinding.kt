package puzzle.core.frontend.semantics.binding

import puzzle.core.frontend.ast.ImportScope
import puzzle.core.frontend.ast.expression.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.model.findContext
import puzzle.core.frontend.semantics.scope.BlockScope
import puzzle.core.frontend.semantics.scope.PzlScope
import puzzle.core.frontend.semantics.scope.RootScope
import puzzle.core.frontend.semantics.scope.findFileScope
import puzzle.core.frontend.semantics.symbol.PzlSymbol

context(_: FileContext)
fun List<Expression>.declares(parent: PzlScope<FileContext>) {
	this.forEach { it.declare(parent) }
}

context(_: FileContext)
fun Expression.declare(parent: PzlScope<FileContext>) {
	when (this) {
		is AsExpression -> this.declare(parent)
		is BinaryExpression -> this.declare(parent)
		is ElvisExpression -> this.declare(parent)
		is OracleExpression -> this.declare(parent)
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
		is Identifier -> this.declare(parent)
		else -> {}
	}
}

context(_: FileContext)
private fun AsExpression.declare(parent: PzlScope<FileContext>) {
	this.expression.declare(parent)
}

context(_: FileContext)
private fun BinaryExpression.declare(parent: PzlScope<FileContext>) {
	this.left.declare(parent)
	this.right.declare(parent)
}

context(_: FileContext)
private fun ElvisExpression.declare(parent: PzlScope<FileContext>) {
	this.left.declare(parent)
	this.right.declare(parent)
}

context(_: FileContext)
private fun OracleExpression.declare(parent: PzlScope<FileContext>) {
	this.left.declare(parent)
	this.right.declare(parent)
}

context(_: FileContext)
private fun GroupingExpression.declare(parent: PzlScope<FileContext>) {
	this.expression.declare(parent)
}

context(_: FileContext)
private fun IfExpression.declare(parent: PzlScope<FileContext>) {
	this.condition.declare(parent)
	val thenScope = BlockScope(parent)
	this.thenBody.declares(thenScope)
	val elseScope = BlockScope(parent)
	this.elseBody.declares(elseScope)
}

context(_: FileContext)
private fun InvokeExpression.declare(parent: PzlScope<FileContext>) {
	this.callee.declare(parent)
	this.arguments.declares(parent)
}

context(_: FileContext)
private fun ReturnExpression.declare(parent: PzlScope<FileContext>) {
	this.expression?.declare(parent)
}

context(_: FileContext)
private fun BreakExpression.declare(parent: PzlScope<FileContext>) {
	this.expression?.declare(parent)
}

context(_: FileContext)
private fun IsExpression.declare(parent: PzlScope<FileContext>) {
	this.expression.declare(parent)
}

context(file: FileContext)
private fun LambdaExpression.declare(parent: PzlScope<FileContext>) {
	val scope = BlockScope(parent)
	this.references.declares(scope)
	this.body.declares(scope)
}

context(_: FileContext)
private fun StringLiteral.Template.declare(parent: PzlScope<FileContext>) {
	this.parts.forEach { part ->
		if (part !is StringLiteral.Template.Part.Expression) return@forEach
		part.expression.declare(parent)
	}
}

context(_: FileContext)
private fun LoopExpression.declare(parent: PzlScope<FileContext>) {
	val scope = BlockScope(parent)
	this.body.declares(scope)
}

context(_: FileContext)
private fun MatchPatternExpression.declare(parent: PzlScope<FileContext>) {
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
private fun MatchConditionExpression.declare(parent: PzlScope<FileContext>) {
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
private fun MemberAccessExpression.declare(parent: PzlScope<FileContext>) {
	this.receiver.declare(parent)
}

context(_: FileContext)
private fun MemberReferenceExpression.declare(parent: PzlScope<FileContext>) {
	this.receiver?.declare(parent)
}

context(_: FileContext)
private fun MultiValueExpression.declare(parent: PzlScope<FileContext>) {
	this.expressions.declares(parent)
}

context(_: FileContext)
private fun NonNullAssertionExpression.declare(parent: PzlScope<FileContext>) {
	this.receiver.declare(parent)
}

context(_: FileContext)
private fun TernaryExpression.declare(parent: PzlScope<FileContext>) {
	this.condition.declare(parent)
	this.thenExpression.declare(parent)
	this.elseExpression.declare(parent)
}

context(_: FileContext)
private fun PrefixUnaryExpression.declare(parent: PzlScope<FileContext>) {
	this.expression.declare(parent)
}

context(_: FileContext)
private fun PostfixUnaryExpression.declare(parent: PzlScope<FileContext>) {
	this.expression.declare(parent)
}

context(_: FileContext)
private fun Identifier.declare(parent: PzlScope<FileContext>) {
	parent.lookupSymbol(this.value)
}

context(file: FileContext)
private fun PzlScope<FileContext>.lookupSymbol(name: String): PzlSymbol {
	var symbols = this.lookupLocal(name)
	if (symbols.isNotEmpty()) {
		return symbols.last()
	}
	val fileScope = this.findFileScope()
	val segments = fileScope.owner.node.importDirectives.filter { directive ->
		if (directive.scope != ImportScope.SINGLE) return@filter false
		if (directive.alias?.value == name) return@filter true
		directive.segments.last() == name
	}.firstOrNull()?.segments
	if (segments != null) {
		var scope: PzlScope<*> = RootScope
		val module = findContext<ModuleContext>()
		println(module.deps)
//		segments.forEach { segment ->

//		}
	}
	TODO()
}