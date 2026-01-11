package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.semantics.scope.Scope

sealed interface Symbol {
	
	val name: String?
	
	val kind: SymbolKind
	
	val owner: Scope
	
	val node: AstNode?
	
	val visibility: Visibility?
}

enum class SymbolKind {
	FUN,
	CTOR,
	
	CLASS,
	TRAIT,
	OBJECT,
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
	
	PROJECT,
	MODULE,
	FILE
}