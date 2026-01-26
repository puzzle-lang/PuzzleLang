package puzzle.sema.scope

sealed interface InitContainer {
	
	val initBlocks: MutableList<BlockScope>
}