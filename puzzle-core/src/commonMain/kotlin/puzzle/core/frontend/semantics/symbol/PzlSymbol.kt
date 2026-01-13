package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.semantics.scope.PzlScope

sealed interface PzlSymbol {
	
	val name: String?
		get() = null
	
	val kind: PzlSymbolKind
	
	val owner: PzlScope?
		get() = null
	
	val node: PzlAstNode?
		get() = null
	
	val visibility: Visibility?
		get() = null
	
	val scope: PzlScope?
		get() = null
}

enum class PzlSymbolKind {
	FUN,
	CTOR,
	
	CLASS,
	TRAIT,
	OBJECT,
	ERROR,
	STRUCT,
	ENUM,
	ANNOTATION,
	EXTENSION,
	MIXIN,
	TYPE_ALIAS,
	TYPE_PARAMETER,
	
	PROPERTY,
	PROPERTY_GETTER,
	PROPERTY_SETTER,
	
	LOCAL,
	
	ENUM_ENTRY,
	
	ROOT,
	PROJECT,
	MODULE,
	FILE,
	PACKAGE
}