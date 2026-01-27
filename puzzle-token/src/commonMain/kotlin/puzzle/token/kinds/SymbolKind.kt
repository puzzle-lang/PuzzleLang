@file:Suppress("ClassName")

package puzzle.token.kinds

import puzzle.core.collection.fastSetOf

sealed class SymbolKind(
	override val value: String,
) : PzlTokenKind {
	
	companion object {
		
		val kinds = OperatorKind.kinds +
				AssignmentKind.kinds +
				AccessKind.kinds +
				BracketKind.kinds +
				SeparatorKind.kinds +
				COLON + QUESTION +
				ELVIS + ORACLE +
				AT + ARROW +
				TRIPLE_DOT +
				PLUS_DOUBLE_DOT
	}
	
	object COLON : SymbolKind(":")
	
	object QUESTION : SymbolKind("?")
	
	object ELVIS : SymbolKind("?:")
	
	object ORACLE : SymbolKind("|:")
	
	object AT : SymbolKind("@")
	
	object ARROW : SymbolKind("->")
	
	object TRIPLE_DOT : SymbolKind("...")
	
	object PLUS_DOUBLE_DOT : SymbolKind("+..")
}

sealed class OperatorKind(
	value: String,
	val priority: Int,
	val assoc: Assoc,
) : SymbolKind(value) {
	
	companion object {
		
		val kinds = fastSetOf(
			DOUBLE_PLUS, DOUBLE_MINUS,
			NOT, BIT_NOT,
			PLUS, MINUS, STAR, SLASH, PERCENT, DOUBLE_STAR,
			RANGE_TO, RANGE_UNTIL,
			SHL, SHR, USHR,
			EQUALS, NOT_EQUALS, TRIPLE_EQUALS, TRIPLE_NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS,
			IN, NOT_IN,
			BIT_AND, BIT_XOR, BIT_OR,
			AND,
			OR
		)
	}
	
	object DOUBLE_PLUS : OperatorKind("++", 13, Assoc.RIGHT)
	
	object DOUBLE_MINUS : OperatorKind("--", 13, Assoc.RIGHT)
	
	object NOT : OperatorKind("!", 12, Assoc.RIGHT)
	
	object BIT_NOT : OperatorKind("~", 12, Assoc.RIGHT)
	
	object DOUBLE_STAR : OperatorKind("**", 11, Assoc.RIGHT)
	
	object STAR : OperatorKind("*", 10, Assoc.LEFT)
	
	object SLASH : OperatorKind("/", 10, Assoc.LEFT)
	
	object PERCENT : OperatorKind("%", 10, Assoc.LEFT)
	
	object PLUS : OperatorKind("+", 9, Assoc.LEFT)
	
	object MINUS : OperatorKind("-", 9, Assoc.LEFT)
	
	object RANGE_TO : OperatorKind("..", 8, Assoc.LEFT)
	
	object RANGE_UNTIL : OperatorKind("..<", 8, Assoc.LEFT)
	
	object SHL : OperatorKind("<<", 7, Assoc.LEFT)
	
	object SHR : OperatorKind(">>", 7, Assoc.LEFT)
	
	object USHR : OperatorKind(">>>", 7, Assoc.LEFT)
	
	object EQUALS : OperatorKind("==", 6, Assoc.NONE)
	
	object NOT_EQUALS : OperatorKind("!=", 6, Assoc.NONE)
	
	object TRIPLE_EQUALS : OperatorKind("===", 6, Assoc.NONE)
	
	object TRIPLE_NOT_EQUALS : OperatorKind("!==", 6, Assoc.NONE)
	
	object GT : OperatorKind(">", 6, Assoc.NONE)
	
	object GT_EQUALS : OperatorKind(">=", 6, Assoc.NONE)
	
	object LT : OperatorKind("<", 6, Assoc.NONE)
	
	object LT_EQUALS : OperatorKind("<=", 6, Assoc.NONE)
	
	object IN : OperatorKind("in", 6, Assoc.NONE)
	
	object NOT_IN : OperatorKind("!in", 6, Assoc.NONE)
	
	object BIT_AND : OperatorKind("&", 5, Assoc.LEFT)
	
	object BIT_XOR : OperatorKind("^", 4, Assoc.LEFT)
	
	object BIT_OR : OperatorKind("|", 3, Assoc.LEFT)
	
	object AND : OperatorKind("&&", 2, Assoc.LEFT)
	
	object OR : OperatorKind("||", 1, Assoc.LEFT)
}

enum class Assoc {
	LEFT,
	NONE,
	RIGHT
}

sealed class AssignmentKind(
	value: String,
	val isCompound: Boolean,
) : SymbolKind(value) {
	
	companion object {
		
		val kinds = fastSetOf(
			ASSIGN, QUESTION_ASSIGN,
			PLUS_ASSIGN, MINUS_ASSIGN, STAR_ASSIGN, SLASH_ASSIGN, PERCENT_ASSIGN,
		)
	}
	
	object ASSIGN : AssignmentKind("=", false)
	
	object QUESTION_ASSIGN : AssignmentKind("?=", false)
	
	object PLUS_ASSIGN : AssignmentKind("+=", true)
	
	object MINUS_ASSIGN : AssignmentKind("-=", true)
	
	object STAR_ASSIGN : AssignmentKind("*=", true)
	
	object SLASH_ASSIGN : AssignmentKind("/=", true)
	
	object PERCENT_ASSIGN : AssignmentKind("%=", true)
}

sealed class AccessKind(value: String) : SymbolKind(value) {
	
	companion object {
		
		val kinds = fastSetOf(DOT, QUESTION_DOT, DOUBLE_COLON)
	}
	
	object DOT : AccessKind(".")
	
	object QUESTION_DOT : AccessKind("?.")
	
	object DOUBLE_COLON : AccessKind("::")
}

sealed class BracketKind(value: String) : SymbolKind(value) {
	
	companion object {
		
		val kinds = Start.kinds + End.kinds
	}
	
	sealed class Start(value: String) : BracketKind(value) {
		
		companion object {
			
			val kinds = fastSetOf(LPAREN, LBRACKET, LBRACE)
		}
		
		object LPAREN : Start("(")
		
		object LBRACKET : Start("[")
		
		object LBRACE : Start("{")
	}
	
	sealed class End(value: String) : BracketKind(value) {
		
		companion object {
			
			val kinds = fastSetOf(RPAREN, RBRACKET, RBRACE)
		}
		
		object RPAREN : End(")")
		
		object RBRACKET : End("]")
		
		object RBRACE : End("}")
	}
}

sealed class SeparatorKind(value: String) : SymbolKind(value) {
	
	companion object {
		
		val kinds = fastSetOf(COMMA, SEMICOLON)
	}
	
	object COMMA : SeparatorKind(",")
	
	object SEMICOLON : SeparatorKind(";")
}