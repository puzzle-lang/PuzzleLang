package puzzle.frontend.parser.parser.type

import puzzle.ast.type.SuperConstructorCall
import puzzle.ast.type.SuperType
import puzzle.ast.type.SuperTypeReference
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.ArgumentTarget
import puzzle.frontend.parser.parser.expression.parseArguments
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseSuperTypes(target: SuperTypeTarget): List<SuperType> {
	if (!cursor.match(COLON)) {
		return emptyList()
	}
	val superTypes = mutableListOf<SuperType>()
	do {
		superTypes += parseSuperType(target)
	} while (cursor.match(COMMA))
	superTypes.check()
	return superTypes
}

@Suppress("UNCHECKED_CAST")
fun List<SuperType>.safeAsSuperTypeReferences(): List<SuperTypeReference> {
	return this as List<SuperTypeReference>
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseSuperType(target: SuperTypeTarget): SuperType {
	val type = parseNamedType()
	if (!cursor.match(LPAREN)) {
		return SuperTypeReference(type, type.location)
	}
	if (!target.allowConstructorCall) {
		syntaxError("${target.label} 不允许使用构造函数调用", type)
	}
	val arguments = parseArguments(ArgumentTarget.SUPER_CONSTRUCTOR_CALL)
	val end = cursor.previous.location
	return SuperConstructorCall(type, arguments, type.location span end)
}

context(_: FileContext)
private fun List<SuperType>.check() {
	var isUsedConstructorCall = false
	this.forEach {
		if (it is SuperConstructorCall) {
			if (isUsedConstructorCall) {
				syntaxError("只允许继承单个类", it)
			}
			isUsedConstructorCall = true
		}
	}
}

enum class SuperTypeTarget(
	val label: String,
	val allowConstructorCall: Boolean,
) {
	CLASS("类", allowConstructorCall = true),
	OBJECT("单例对象", allowConstructorCall = true),
	TRAIT("特征", allowConstructorCall = false),
	EXTENSION("扩展", allowConstructorCall = false),
	STRUCT("结构体", allowConstructorCall = false),
	ENUM("结构体", allowConstructorCall = false)
}