package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.semantics.scope.FileScope
import puzzle.core.frontend.semantics.scope.PzlScope

class FileSymbol(
	override val name: String?,
	override val node: AstFile,
) : PzlSymbol {
	
	override lateinit var owner: PzlScope
	
	override lateinit var scope: FileScope
	
	override val kind = PzlSymbolKind.FILE
}