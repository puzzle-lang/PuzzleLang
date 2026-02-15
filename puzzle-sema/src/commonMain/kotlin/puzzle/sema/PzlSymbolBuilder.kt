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
    fun buildPackageSymbol(): FileSymbol {
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
                    buildProjectSymbol()
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
    private fun buildProjectSymbol() {
        val symbol = ProjectSymbol(project.name.toIdentifier(), RootScope)
        RootScope.declare(symbol)
        val scope = ProjectScope(RootScope, symbol)
        symbol.scope = scope
        project.modules.forEach { module ->
            context(module) {
                buildModuleSymbol(scope)
            }
        }
    }

    context(module: ModuleContext, _: ProjectContext)
    private fun buildModuleSymbol(parent: ProjectScope) {
        val symbol = ModuleSymbol(module.name.toIdentifier(), parent, module.group)
        parent.declare(symbol)
        val scope = ModuleScope(parent, symbol)
        symbol.scope = scope
        module.files.forEach { file ->
            context(file) {
                buildPackageSymbol(scope)
            }
        }
    }

    context(file: FileContext, _: ModuleContext)
    private fun buildPackageSymbol(parent: ModuleScope) {
        val node = file[AstFileAttachment::class].value
        val segments = node.packageDirective.segments
        var parent: PzlScope<*> = parent
        segments.forEach { segment ->
            var symbol = parent.lookupLocal(segment)
                .find { it is PackageSymbol } as? PackageSymbol
            if (symbol == null) {
                symbol = PackageSymbol(segment.toIdentifier(), parent)
                when (parent) {
                    is PackageScope -> parent.declare(symbol)
                    is ModuleScope -> parent.declare(symbol)
                    else -> error("不允许的 Scope 类型: ${parent::class.simpleName}")
                }
                val scope = PackageScope(parent, symbol)
                symbol.scope = scope
            }
            parent = symbol.scope
        }
        parent as PackageScope
        val symbol = file[FileSymbolAttachment::class].value
        parent.declare(symbol)
        symbol.owner = parent
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