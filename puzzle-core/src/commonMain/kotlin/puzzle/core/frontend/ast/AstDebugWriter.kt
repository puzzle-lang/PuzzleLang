package puzzle.core.frontend.ast

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.model.ProjectContext
import puzzle.core.frontend.model.RootContext
import puzzle.core.util.PathWrapper
import puzzle.core.util.path

object AstDebugWriter {
	
	private val json = Json {
		prettyPrint = true
		encodeDefaults = true
		classDiscriminator = "class"
		ignoreUnknownKeys = true
		serializersModule = AstSerializersModule
	}
	
	private val lock = Mutex()
	
	context(context: RootContext, scope: CoroutineScope)
	suspend fun write(projectPath: PathWrapper) {
		val buildAstPath = path(projectPath, "build", "ast")
		if (buildAstPath.exists()) {
			buildAstPath.deleteAll()
		}
		val jobs = context.projects.flatMap { project ->
			project.modules.flatMap { module ->
				module.files.mapNotNull { file ->
					val node = file.node
					if (node.sourcePath == null && !node.builtin) return@mapNotNull null
					scope.launch(Dispatchers.IO) {
						val astPath = if (node.builtin) {
							getBuiltinAstPath(buildAstPath, project, module, node)
						} else {
							getAstPath(buildAstPath, project, node)
						}
						val parent = astPath.parent ?: return@launch
						val text = withContext(Dispatchers.Default) {
							json.encodeToString(node)
						}
						if (!parent.exists()) {
							lock.withLock {
								if (!parent.exists()) {
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
		buildAstPath: PathWrapper,
		project: ProjectContext,
		module: ModuleContext,
		node: AstFile,
	): PathWrapper {
		val astName = node.name.removeSuffix(".pzl") + ".json"
		return path(buildAstPath, project.name, module.name, "src", "main", "puzzle", astName)
	}
	
	private suspend fun getAstPath(
		buildAstPath: PathWrapper,
		project: ProjectContext,
		node: AstFile,
	): PathWrapper {
		val astPath = node.sourcePath!!.absolutePath.removePrefix(project.path!!.parent!!.absolutePath + "/").removeSuffix(".pzl") + ".json"
		return path(buildAstPath, *astPath.split('/').toTypedArray())
	}
}