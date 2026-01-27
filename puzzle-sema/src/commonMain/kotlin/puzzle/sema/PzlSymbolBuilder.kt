package puzzle.sema

import puzzle.ast.AstFileAttachment
import puzzle.ast.expression.toIdentifier
import puzzle.core.context.FileContext
import puzzle.core.context.ModuleContext
import puzzle.core.context.ProjectContext
import puzzle.core.context.RootContext
import puzzle.sema.binding.declares
import puzzle.sema.deferred.getDeferredDeclarers
import puzzle.sema.scope.*
import puzzle.sema.symbol.*

object PzlSymbolBuilder {
	
	context(file: FileContext)
	fun buildFileSymbol(): FileSymbol {
		val astFile = file[AstFileAttachment::class].value
		val symbol = FileSymbol(file.name.toIdentifier(), astFile)
		symbol.checkImportDuplicate()
		val scope = FileScope(symbol)
		symbol.scope = scope
		astFile.declarations.declares(scope)
		return symbol
	}
	
	fun buildRootSymbol() {
		context(RootContext) {
			RootContext.projects.forEach { project ->
				context(project) {
					val symbol = buildProjectSymbol()
					RootScope.declare(symbol)
				}
			}
		}
		forEachAllFileContext {
			getDeferredDeclarers().forEach {
				it.declares()
			}
		}
	}
	
	context(project: ProjectContext, _: RootContext)
	private fun buildProjectSymbol(): ProjectSymbol {
		val symbol = ProjectSymbol(project.name.toIdentifier(), RootScope)
		RootScope.declare(symbol)
		val scope = ProjectScope(RootScope, symbol)
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
		val symbol = ModuleSymbol(module.name.toIdentifier(), parent)
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
		val node = file[AstFileAttachment::class].value
		val segments = node.packageDirective?.segments
			?: return file[FileSymbolAttachment::class].value
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
				symbol = PackageSymbol(segment.toIdentifier(), parent)
				when (parent) {
					is PackageScope -> parent.declare(symbol)
					is ModuleScope -> parent.declare(symbol)
					else -> error("不允许的 Scope")
				}
				val scope = PackageScope(parent, symbol)
				symbol.scope = scope
			}
			parent = symbol.scope
		}
		val symbol = file[FileSymbolAttachment::class].value
		when (parent) {
			is PackageScope -> parent.declare(symbol)
			is ModuleScope -> parent.declare(symbol)
			else -> error("不允许的 Scope")
		}
		symbol.owner = parent
		return symbol
	}
	
	private fun forEachAllFileContext(
		action: context(FileContext) () -> Unit,
	) {
		RootContext.projects.forEach { project ->
			project.modules.forEach { module ->
				module.files.forEach { file ->
					action(file)
				}
			}
		}
	}
}