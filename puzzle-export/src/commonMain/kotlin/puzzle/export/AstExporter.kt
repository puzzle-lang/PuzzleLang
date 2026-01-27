package puzzle.export

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import puzzle.ast.AstFileAttachment
import puzzle.core.context.FileContext
import puzzle.core.context.ModuleContext
import puzzle.core.context.ProjectContext
import puzzle.core.context.RootContext
import puzzle.core.io.FilePath
import puzzle.core.io.path

object AstExporter {
	
	private val lock = Mutex()
	
	context(scope: CoroutineScope)
	suspend fun export(projectPath: FilePath) {
		val buildAstPath = path(projectPath, "build", "ast")
		if (buildAstPath.exists) {
			buildAstPath.delete()
		}
		val jobs = RootContext.projects.flatMap { project ->
			project.modules.flatMap { module ->
				module.files.mapNotNull { file ->
					val node = file[AstFileAttachment::class].value
					if (file.path == null && !node.builtin) return@mapNotNull null
					scope.launch(Dispatchers.IO) {
						val astPath = if (node.builtin) {
							getBuiltinAstPath(buildAstPath, project, module, file)
						} else {
							getAstPath(buildAstPath, project, file)
						}
						val parent = astPath.parent ?: return@launch
						val text = withContext(Dispatchers.Default) {
							json.encodeToString(node)
						}
						if (!parent.exists) {
							lock.withLock {
								if (!parent.exists) {
									parent.createDirectories()
								}
							}
						}
						try {
							astPath.writeText(text)
						} catch (_: Exception) {
						
						}
					}
				}
			}
		}
		jobs.joinAll()
	}
	
	private fun getBuiltinAstPath(
		buildAstPath: FilePath,
		project: ProjectContext,
		module: ModuleContext,
		file: FileContext,
	): FilePath {
		val astName = file.name.removeSuffix(".pzl") + ".json"
		return path(buildAstPath, project.name, module.name, "src", "main", "puzzle", astName)
	}
	
	private fun getAstPath(
		buildAstPath: FilePath,
		project: ProjectContext,
		file: FileContext,
	): FilePath {
		val astPath = file.path!!.absolutePath.removePrefix(project.path!!.parent!!.absolutePath + "/").removeSuffix(".pzl") + ".json"
		return path(buildAstPath, *astPath.split('/').toTypedArray())
	}
}