package puzzle.core.frontend.semantics

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.model.ProjectContext
import puzzle.core.frontend.model.RootContext
import puzzle.core.frontend.semantics.binding.declare
import puzzle.core.frontend.semantics.binding.declares
import puzzle.core.frontend.semantics.scope.*
import puzzle.core.frontend.semantics.symbol.*

object PzlSymbolBuilder {
	
	context(file: FileContext)
	fun buildFileSymbol(): FileSymbol {
		val node = file.node
		val symbol = FileSymbol(node.name, node)
		val scope = FileScope(symbol)
		symbol.scope = scope
		node.declarations.declares(scope)
		return symbol
	}
	
	context(root: RootContext)
	fun buildRootSymbol(): RootSymbol {
		val symbol = RootSymbol()
		val scope = RootScope(symbol)
		symbol.scope = scope
		root.projects.forEach { project ->
			context(project) {
				val symbol = buildProjectSymbol(scope)
				scope.declare(symbol)
			}
		}
		root.forEachFileContext {
			this.deferredExpressions.forEach {
				it.expression.declare(it.parent)
			}
		}
		return symbol
	}
	
	context(project: ProjectContext, _: RootContext)
	private fun buildProjectSymbol(parent: RootScope): ProjectSymbol {
		val symbol = ProjectSymbol(project.name, parent)
		parent.declare(symbol)
		val scope = ProjectScope(parent, symbol)
		symbol.scope = scope
		project.modules.forEach { module ->
			context(module) {
				val symbol = buildModuleSymbol(scope)
				scope.declare(symbol)
			}
		}
		return symbol
	}
	
	context(module: ModuleContext, _: ProjectContext)
	private fun buildModuleSymbol(parent: ProjectScope): ModuleSymbol {
		val symbol = ModuleSymbol(module.name, parent)
		parent.declare(symbol)
		val scope = ModuleScope(parent, symbol)
		symbol.scope = scope
		module.files.forEach { file ->
			context(file) {
				val symbol = buildFileSymbol(scope)
				scope.declare(symbol)
			}
		}
		return symbol
	}
	
	context(file: FileContext, _: ModuleContext)
	private fun buildFileSymbol(parent: ModuleScope): FileSymbol {
		val segments = file.node.packageDirective?.segments
			?: return file.symbol
		var parent: PzlScope<*> = parent
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
				when (parent) {
					is PackageScope -> parent.declare(symbol)
					is ModuleScope -> parent.declare(symbol)
					else -> error("不允许的 Scope")
				}
				val scope = PackageScope(parent, symbol)
				symbol.scope = scope
			}
			parent = symbol.scope!!
		}
		val symbol = file.symbol
		when (parent) {
			is PackageScope -> parent.declare(symbol)
			is ModuleScope -> parent.declare(symbol)
			else -> error("不允许的 Scope")
		}
		symbol.owner = parent
		return symbol
	}
	
	private fun RootContext.forEachFileContext(
		action: FileContext.() -> Unit,
	) {
		this.projects.forEach { project ->
			project.modules.forEach { module ->
				module.files.forEach { file ->
					action(file)
				}
			}
		}
	}
}