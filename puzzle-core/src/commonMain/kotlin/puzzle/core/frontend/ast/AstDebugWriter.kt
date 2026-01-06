package puzzle.core.frontend.ast

import kotlinx.serialization.json.Json
import puzzle.core.frontend.model.AstProject
import puzzle.core.util.PathWrapper
import puzzle.core.util.format
import puzzle.core.util.path
import kotlin.time.DurationUnit
import kotlin.time.measureTime

object AstDebugWriter {
	
	private val json = Json {
		prettyPrint = true
		encodeDefaults = true
		classDiscriminator = "class"
		ignoreUnknownKeys = true
		serializersModule = AstSerializersModule
	}
	
	fun write(projectPath: PathWrapper, project: AstProject) {
		val duration = measureTime {
			val buildAstPath = path(projectPath, "build", "ast")
			if (buildAstPath.exists()) {
				buildAstPath.deleteAll()
			}
			project.modules.forEach { module ->
				module.nodes.forEach { node ->
					if (node.sourcePath == null && !node.isBuiltin) return@forEach
					val astPath = if (node.isBuiltin) {
						getBuiltinPath(buildAstPath, module.name, node.name)
					} else {
						getAstPath(projectPath, buildAstPath, node.sourcePath!!)
					}
					if (astPath.parent == null) return@forEach
					if (!astPath.parent!!.exists()) {
						astPath.parent!!.createDirectories()
					}
					astPath.writeText(json.encodeToString(node))
				}
			}
		}
		println("AST 保存用时: ${duration.format()}")
	}
	
	private fun getBuiltinPath(buildPath: PathWrapper, moduleName: String, nodeName: String): PathWrapper {
		val nodeName = nodeName.removeSuffix(".pzl")
		return path(buildPath, moduleName, "src", "main", "puzzle", "$nodeName.json")
	}
	
	private fun getAstPath(
		projectPath: PathWrapper,
		buildPath: PathWrapper,
		sourcePath: PathWrapper,
	): PathWrapper {
		val path = sourcePath.absolutePath.removePrefix(projectPath.absolutePath).removeSuffix(".pzl")
		return path(buildPath, "$path.json")
	}
}