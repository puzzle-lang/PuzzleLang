package puzzle.core.frontend.semantics

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.model.ProjectContext
import puzzle.core.frontend.model.RootContext
import puzzle.core.frontend.semantics.binding.declares
import puzzle.core.frontend.semantics.scope.*
import puzzle.core.frontend.semantics.symbol.*

object PzlSymbolBuilder {
	
	context(context: FileContext)
	fun buildFileSymbol(): FileSymbol {
		val node = context.node
		val symbol = FileSymbol(node.name, node)
		val scope = FileScope(symbol)
		symbol.scope = scope
		node.declarations.declares(scope)
		return symbol
	}
	
	context(context: RootContext)
	fun buildRootSymbol(): RootSymbol {
		val symbol = RootSymbol()
		val scope = RootScope(symbol)
		symbol.scope = scope
		context.projects.forEach { project ->
			context(project) {
				val symbol = buildProjectSymbol(scope)
				scope.declare(symbol)
			}
		}
		return symbol
	}
	
	context(context: ProjectContext)
	private fun buildProjectSymbol(parent: RootScope): ProjectSymbol {
		val symbol = ProjectSymbol(context.name, parent)
		parent.declare(symbol)
		val scope = ProjectScope(parent, symbol)
		symbol.scope = scope
		context.modules.forEach { module ->
			context(module) {
				val symbol = buildModuleSymbol(scope)
				scope.declare(symbol)
			}
		}
		return symbol
	}
	
	context(context: ModuleContext)
	private fun buildModuleSymbol(parent: ProjectScope): ModuleSymbol {
		val symbol = ModuleSymbol(context.name, parent)
		parent.declare(symbol)
		val scope = ModuleScope(parent, symbol)
		symbol.scope = scope
		context.files.forEach { file ->
			context(file) {
				val symbol = buildFileSymbol(scope)
				scope.declare(symbol)
			}
		}
		return symbol
	}
	
	context(context: FileContext)
	private fun buildFileSymbol(parent: ModuleScope): FileSymbol {
		val segments = context.node.packageDirective?.segments
			?: return context.symbol
		var parent: PzlScope = parent
		var needCreate = false
		segments.forEach { segment ->
			var symbol: PackageSymbol? = null
			if (!needCreate) {
				symbol = parent.lookup(segment)
					.find { it is PackageSymbol } as? PackageSymbol
				if (symbol == null) {
					needCreate = true
				}
			}
			if (symbol == null) {
				symbol = PackageSymbol(segment, parent)
				parent.declare(symbol)
				val scope = PackageScope(parent, symbol)
				symbol.scope = scope
			}
			parent = symbol.scope!!
		}
		val symbol = context.symbol
		parent.declare(symbol)
		symbol.owner = parent
		return symbol
	}
}