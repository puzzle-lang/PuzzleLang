package puzzle.core.frontend.parser.parser.parameter.parameter

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.parser.parser.check
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseExpressionChain
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parseQuantifier
import puzzle.core.frontend.parser.parser.parseAnnotationCalls
import puzzle.core.frontend.parser.parser.parseModifiers
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SymbolTokenKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseParameters(target: ParameterTarget): List<Parameter> {
	if (!cursor.match(LPAREN)) {
		if (target.allowWithoutParen) return emptyList()
		syntaxError("${target.label} 缺少 '('", cursor.current)
	}
	if (cursor.match(RPAREN)) return emptyList()
	val parameters = buildList {
		do {
			this += parseParameter(target)
			if (!cursor.check(RPAREN)) {
				cursor.expect(COMMA, "型参列表缺少 ','")
			}
		} while (!cursor.match(RPAREN))
	}
	parameters.check()
	return parameters
}

context(_: PzlContext)
private fun List<Parameter>.check() {
	var existsTypeQuantifier = false
	this.forEach {
		if (it.quantifier == null) return@forEach
		if (existsTypeQuantifier) {
			syntaxError("只允许使用一个包含数量修饰符的参数", it.quantifier)
		}
		existsTypeQuantifier = true
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseParameter(target: ParameterTarget): Parameter {
	val start = cursor.current.location
	val annotationCalls = parseAnnotationCalls()
	val modifiers = parseModifiers()
	modifiers.check(target.modifierTarget)
	val name = parseIdentifier(IdentifierTarget.PARAMETER)
	cursor.expect(COLON, "型参缺少 ':'")
	val type = parseTypeReference(allowLambda = target.allowLambda)
	val quantifier = parseQuantifier()
	if (quantifier != null && !target.allowVarargQuantifier) {
		syntaxError("${target.label} 参数不支持可变量词", cursor.previous)
	}
	val defaultExpression = if (cursor.match(ASSIGN)) {
		if (quantifier != null) {
			syntaxError("${target.label} 可变参数不允许设置默认值", cursor.previous)
		}
		parseExpressionChain()
	} else null
	val end = cursor.previous.location
	return Parameter(
		name = name,
		modifiers = modifiers,
		type = type,
		annotationCalls = annotationCalls,
		quantifier = quantifier,
		defaultExpression = defaultExpression,
		location = start span end
	)
}