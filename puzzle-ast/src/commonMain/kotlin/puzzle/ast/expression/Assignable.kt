package puzzle.ast.expression

sealed interface Assignable

sealed interface DirectAssignable : Assignable

sealed interface CompoundAssignable : Assignable

sealed interface CompoundAssignableProxy : Assignable {
	
	val inner: Expression
}