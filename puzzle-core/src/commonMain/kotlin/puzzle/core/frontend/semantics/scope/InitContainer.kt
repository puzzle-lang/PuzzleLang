package puzzle.core.frontend.semantics.scope

sealed interface InitContainer {
	
	val initBlocks: MutableList<BlockScope>
}