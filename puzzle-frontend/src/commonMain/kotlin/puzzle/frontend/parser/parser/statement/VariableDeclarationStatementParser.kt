package puzzle.frontend.parser.parser.statement

import puzzle.ast.statement.DestructureVariableSpec
import puzzle.ast.statement.SingleVariableSpec
import puzzle.ast.statement.Variable
import puzzle.ast.statement.VariableDeclarationStatement
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.isAnonymousBinding
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseExpressionChain
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.AssignmentKind.ASSIGN
import puzzle.token.kinds.BracketKind.End.RBRACKET
import puzzle.token.kinds.BracketKind.Start.LBRACKET
import puzzle.token.kinds.ModifierKind.VAL
import puzzle.token.kinds.ModifierKind.VAR
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseVariableDeclarationStatement(): VariableDeclarationStatement {
	val start = cursor.previous.location
	val isMutable = cursor.previous.kind == VAR
	val variableSpec = if (cursor.match(LBRACKET)) {
		parseDestructureVariableSpec(start, isMutable)
	} else {
		parseSingleVariableSpec(start, isMutable)
	}
	val initializer = if (cursor.match(ASSIGN)) {
		parseExpressionChain()
	} else {
		when (variableSpec) {
			is SingleVariableSpec if variableSpec.variable.type == null -> {
				syntaxError("变量缺少类型", cursor.previous.location.end)
			}
			
			is DestructureVariableSpec -> {
				syntaxError("解构变量必须赋值", cursor.previous.location.end)
			}
			
			else -> null
		}
	}
	val end = cursor.previous.location
	return VariableDeclarationStatement(
		variableSpec = variableSpec,
		initializer = initializer,
		location = start span end
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseSingleVariableSpec(
	start: SourceLocation,
	isMutable: Boolean,
): SingleVariableSpec {
	val name = parseIdentifier(IdentifierTarget.VARIABLE)
	if (name.isAnonymousBinding && isMutable) {
		syntaxError("匿名参数不允许使用 var 可变修饰符", cursor.offset(-2))
	}
	val type = if (cursor.match(COLON)) {
		parseTypeReference(allowLambda = true)
	} else null
	val end = cursor.previous.location
	val variable = Variable(isMutable, name, type, start span end)
	return SingleVariableSpec(variable)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseDestructureVariableSpec(
	start: SourceLocation,
	defaultMutable: Boolean,
): DestructureVariableSpec {
	val variables = buildList {
		while (!cursor.match(RBRACKET)) {
			val start = cursor.current.location
			var isMutable = when {
				cursor.match(VAR) -> true
				cursor.match(VAL) -> false
				else -> null
			}
			val name = parseIdentifier(IdentifierTarget.VARIABLE_DESTRUCTURE)
			if (isMutable == null) {
				isMutable = if (name.isAnonymousBinding) false else defaultMutable
			}
			if (isMutable!! && name.isAnonymousBinding) {
				syntaxError("匿名解构变量不允许使用 var 可变修饰符", cursor.offset(-2))
			}
			val type = if (cursor.match(COLON)) {
				parseTypeReference(allowLambda = true)
			} else null
			val end = cursor.previous.location
			this += Variable(
				isMutable = isMutable,
				name = name,
				type = type,
				location = start span end
			)
			if (!cursor.check(RBRACKET)) {
				cursor.expect(COMMA, "解构变量列表缺少 ','")
			}
		}
	}
	if (variables.isEmpty()) {
		syntaxError("解构变量列表缺少变量", cursor.previous)
	}
	if (variables.all { it.name.isAnonymousBinding }) {
		syntaxError("解构变量列表不允许全部使用匿名绑定", variables.first().name)
	}
	val end = cursor.previous.location
	return DestructureVariableSpec(variables, start span end)
}