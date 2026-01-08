package puzzle.core.frontend.ast

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import puzzle.core.frontend.model.AstModule
import puzzle.core.frontend.model.AstProject
import puzzle.core.frontend.model.AstRoot
import puzzle.core.util.PathWrapper
import puzzle.core.util.format
import puzzle.core.util.path
import kotlin.time.measureTime

object AstDebugWriter {
	
	private val json = Json {
		prettyPrint = true
		encodeDefaults = true
		classDiscriminator = "class"
		ignoreUnknownKeys = true
		serializersModule = AstSerializersModule
	}
	
	private val lock = Mutex()
	
	suspend fun write(projectPath: PathWrapper, root: AstRoot) = coroutineScope {
		val duration = measureTime {
			val buildAstPath = path(projectPath, "build", "ast")
			if (buildAstPath.exists()) {
				buildAstPath.deleteAll()
			}
			val jobs = root.projects.flatMap { project ->
				project.modules.flatMap { module ->
					module.files.mapNotNull { file ->
						if (file.sourcePath == null && !file.builtin) return@mapNotNull null
						launch(Dispatchers.IO) {
							val astPath = if (file.builtin) {
								getBuiltinAstPath(buildAstPath, project, module, file)
							} else {
								getAstPath(buildAstPath, project, file)
							}
							val parent = astPath.parent ?: return@launch
							lock.withLock {
								if (!parent.exists()) {
									parent.createDirectories()
								}
							}
							val text = withContext(Dispatchers.Default) {
								json.encodeToString(file)
							}
							astPath.writeText(text)
						}
					}
				}
			}
			jobs.joinAll()
		}
		println("AST 保存用时: ${duration.format()}")
	}
	
	private fun getBuiltinAstPath(
		buildAstPath: PathWrapper,
		project: AstProject,
		module: AstModule,
		file: AstFile,
	): PathWrapper {
		val astName = file.name.removeSuffix(".pzl") + ".json"
		return path(buildAstPath, project.name, module.name, "src", "main", "puzzle", astName)
	}
	
	private fun getAstPath(
		buildAstPath: PathWrapper,
		project: AstProject,
		file: AstFile,
	): PathWrapper {
		val astPath = file.sourcePath!!.absolutePath.removePrefix(project.path!!.parent!!.absolutePath + "/").removeSuffix(".pzl") + ".json"
		return path(buildAstPath, astPath)
	}
}