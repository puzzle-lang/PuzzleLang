package puzzle.context

import puzzle.base.io.FilePath

class FileContext : Context() {
	
	override lateinit var parent: ModuleContext
	
	var builtin = false
	
	lateinit var path: FilePath
	
	lateinit var lineStarts: IntArray
	
	lateinit var tokens: List<PzlToken>
	
	lateinit var node: AstFile
	
	lateinit var symbol: FileSymbol
	
	val deferredDeclarers = mutableListOf<DeferredDeclarer>()
}