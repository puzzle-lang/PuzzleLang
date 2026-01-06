package puzzle.core.frontend.token.kinds

import puzzle.core.collections.fastSetOf
import puzzle.core.collections.mergeFastSets
import puzzle.core.frontend.token.kinds.AccessorKind.GET
import puzzle.core.frontend.token.kinds.AccessorKind.SET
import puzzle.core.frontend.token.kinds.ContextualKind.*
import puzzle.core.frontend.token.kinds.ModifierKind.*
import puzzle.core.frontend.token.kinds.NamespaceKind.IMPORT
import puzzle.core.frontend.token.kinds.NamespaceKind.PACKAGE

sealed interface KeywordKind : PzlTokenKind {
	
	companion object {
		
		val kinds = mergeFastSets(
			ModifierKind.availableKinds,
			NamespaceKind.kinds,
			DeclarationKind.kinds,
			AccessorKind.kinds,
			ControlFlowKind.kinds,
			JumpKind.kinds,
			ContextualKind.kinds,
			TypeOperatorKind.kinds,
			LiteralKind.keywordKinds
		)
		
		val softKeywords = setOf(
			PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC,
			FINAL,
			OPEN, ABSTRACT, SEALED, OVERRIDE,
			CONST, INNER, IGNORE, LATE, LAZY,
			GET, SET,
			TYPE, REIFIED, CONTEXT, INIT, ON, WITH,
			PACKAGE, IMPORT
		).map { it.value }.toSet()
		
		val hardKeywords = kinds.map { it.value }.toSet() - softKeywords
	}
}

sealed class ModifierKind(
	override val value: String,
	val order: Int,
) : KeywordKind {
	
	companion object {
		
		val availableKinds = fastSetOf(
			PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC,
			FINAL,
			OPEN, ABSTRACT, SEALED, OVERRIDE,
			CONST, INNER, IGNORE, LATE, LAZY, PREFIX, POSTFIX,
			VAR, VAL
		)
	}
	
	object PRIVATE : ModifierKind("private", 0)
	
	object PROTECTED : ModifierKind("protected", 0)
	
	object FILE : ModifierKind("file", 0)
	
	object INTERNAL : ModifierKind("internal", 0)
	
	object MODULE : ModifierKind("module", 0)
	
	object PUBLIC : ModifierKind("public", 0)
	
	object FINAL : ModifierKind("final", 1)
	
	object OPEN : ModifierKind("open", 2)
	
	object ABSTRACT : ModifierKind("abstract", 2)
	
	object SEALED : ModifierKind("sealed", 2)
	
	object OVERRIDE : ModifierKind("override", 2)
	
	object CONST : ModifierKind("const", 3)
	
	object INNER : ModifierKind("inner", 3)
	
	object IGNORE : ModifierKind("ignore", 3)
	
	object LATE : ModifierKind("late", 3)
	
	object LAZY : ModifierKind("lazy", 3)
	
	object PREFIX : ModifierKind("prefix", 3)
	
	object POSTFIX : ModifierKind("postfix", 3)
	
	object VAR : ModifierKind("var", 4)
	
	object VAL : ModifierKind("val", 4)
	
	object BUILTIN : ModifierKind("builtin", -1)
}

sealed class NamespaceKind(
	override val value: String,
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf(PACKAGE, IMPORT)
	}
	
	object PACKAGE : NamespaceKind("package")
	
	object IMPORT : NamespaceKind("import")
}

sealed class DeclarationKind(
	override val value: String,
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf(
			FUN, CLASS, OBJECT, TRAIT, STRUCT, ENUM, ANNOTATION,
			EXTENSION, MIXIN, TYPEALIAS, CTOR
		)
	}
	
	object FUN : DeclarationKind("fun")
	
	object CLASS : DeclarationKind("class")
	
	object OBJECT : DeclarationKind("object")
	
	object TRAIT : DeclarationKind("trait")
	
	object STRUCT : DeclarationKind("struct")
	
	object ENUM : DeclarationKind("enum")
	
	object ANNOTATION : DeclarationKind("annotation")
	
	object EXTENSION : DeclarationKind("extension")
	
	object MIXIN : DeclarationKind("mixin")
	
	object TYPEALIAS : DeclarationKind("typealias")
	
	object CTOR : DeclarationKind("ctor")
}

sealed class AccessorKind(
	override val value: String,
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf(GET, SET)
	}
	
	object GET : AccessorKind("get")
	
	object SET : AccessorKind("set")
}

sealed class ControlFlowKind(
	override val value: String,
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf(IF, ELSE, MATCH, FOR, WHILE, DO, LOOP)
	}
	
	object IF : ControlFlowKind("if")
	
	object ELSE : ControlFlowKind("else")
	
	object MATCH : ControlFlowKind("match")
	
	object FOR : ControlFlowKind("for")
	
	object WHILE : ControlFlowKind("while")
	
	object DO : ControlFlowKind("do")
	
	object LOOP : ControlFlowKind("loop")
}

sealed class JumpKind(
	override val value: String,
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf(BREAK, CONTINUE, RETURN)
	}
	
	object BREAK : JumpKind("break")
	
	object CONTINUE : JumpKind("continue")
	
	object RETURN : JumpKind("return")
}

sealed class ContextualKind(
	override val value: String,
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf(TYPE, REIFIED, CONTEXT, INIT, ON, WITH, THIS, SUPER, OUT)
	}
	
	object TYPE : ContextualKind("type")
	
	object REIFIED : ContextualKind("reified")
	
	object CONTEXT : ContextualKind("context")
	
	object INIT : ContextualKind("init")
	
	object ON : ContextualKind("on")
	
	object WITH : ContextualKind("with")
	
	object THIS : ContextualKind("this")
	
	object SUPER : ContextualKind("super")
	
	object OUT : ContextualKind("out")
}

sealed class TypeOperatorKind(
	override val value: String,
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf(AS, IS)
	}
	
	object AS : TypeOperatorKind("as")
	
	object IS : TypeOperatorKind("is")
}