package puzzle.core.frontend.semantics.symbol

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.FileScope
import puzzle.core.frontend.semantics.scope.PzlScope

class FileSymbol(
	override val name: Identifier?,
	override val node: AstFile,
) : PzlSymbol {
	
	override lateinit var owner: PzlScope<*>
	
	override lateinit var scope: FileScope
	
	override val kind = PzlSymbolKind.FILE
	
	override val isTypeDeclaration = false
	
	context(_: FileContext)
	fun checkImportDuplicate() {
		val names = mutableSetOf<String>()
		node.importDirectives.forEach {
			val name = if (it.alias != null) {
				it.alias.value
			} else {
				it.segments.last()
			}
			if (name in names) {
				syntaxError("冲突的导入, 导入名称 '$name' 存在冲突")
			}
			names += name
		}
	}
}