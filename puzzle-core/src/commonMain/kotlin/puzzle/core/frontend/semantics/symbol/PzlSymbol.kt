package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.semantics.scope.PzlScope

sealed interface PzlSymbol {
	
	val name: Identifier?
		get() = null
	
	val owner: PzlScope<*>?
		get() = null
	
	val node: PzlAstNode?
		get() = null
	
	val visibility: Visibility?
		get() = null
	
	val scope: PzlScope<*>?
		get() = null
	
	val kind: PzlSymbolKind
	
	val isTypeDeclaration: Boolean
}

enum class PzlSymbolKind {
	FUN, CTOR,
	
	CLASS, TRAIT, OBJECT, ERROR, STRUCT, ENUM, ANNOTATION, EXTENSION, MIXIN, TYPEALIAS,
	
	TYPE_PARAMETER, PARAMETER, ENUM_ENTRY,
	
	PROPERTY, PROPERTY_GETTER, PROPERTY_SETTER,
	
	LOCAL,
	
	ROOT, PROJECT, MODULE, FILE, PACKAGE;
	
	override fun toString(): String {
		return this.name.lowercase()
	}
}