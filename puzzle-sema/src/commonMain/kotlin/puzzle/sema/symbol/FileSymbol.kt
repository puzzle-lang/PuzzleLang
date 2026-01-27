package puzzle.sema.symbol

import puzzle.ast.AstFile
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.sema.scope.FileScope
import puzzle.sema.scope.PzlScope
import puzzle.sema.semaError

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
			val name = it.alias?.value ?: it.segments.last()
			if (name in names) {
				semaError("冲突的导入, 导入名称 $name 存在冲突", it)
			}
			names += name
		}
	}
}