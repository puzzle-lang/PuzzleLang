package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.semantics.scope.Scope

sealed interface Symbol<S : Scope<S>> {
	
	val name: String
	
	val kind: SymbolKind
	
	val owner: S
	
	val node: AstNode?
	
	val visibility: Visibility?
}

enum class SymbolKind {
	PACKAGE,
	
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
	
	FUN,
	CTOR,
	
	PROPERTY,
	LOCAL,
	PARAMETER,
	
	ENUM_ENTRY,
	
	LABEL,
	
	PROJECT,
	MODULE,
	FILE
}