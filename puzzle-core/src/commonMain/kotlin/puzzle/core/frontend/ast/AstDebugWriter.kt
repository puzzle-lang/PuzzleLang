package puzzle.core.frontend.ast

import kotlinx.serialization.json.Json
import puzzle.core.frontend.model.AstProject
import puzzle.core.util.PathWrapper

object AstDebugWriter {
	
	private val json = Json {
		prettyPrint = true
		encodeDefaults = true
		classDiscriminator = "class"
		ignoreUnknownKeys = true
		serializersModule = AstSerializersModule
	}
	
	fun write(projectPath: PathWrapper, project: AstProject) {
		val buildPath = PathWrapper(projectPath, "build", "ast")
		if (buildPath.exists()) {
			buildPath.deleteAll()
		}
		project.modules.forEach { module ->
			module.nodes.forEach { node ->
				if (node.path == null && !node.isBuiltin) return@forEach
				val astPath = if (node.isBuiltin) {
					getBuiltinPath(buildPath, module.name, node.name)
				} else {
					getAstPath(projectPath, buildPath, node.path!!)
				}
				if (astPath.parent == null) return@forEach
				if (!astPath.parent!!.exists()) {
					astPath.parent!!.createDirectories()
				}
				astPath.writeText(json.encodeToString(node))
			}
		}
	}
	
	private fun getBuiltinPath(buildPath: PathWrapper, moduleName: String, nodeName: String): PathWrapper {
		val nodeName = nodeName.removeSuffix(".pzl")
		return PathWrapper(buildPath, moduleName, "src", "main", "puzzle", "$nodeName.json")
	}
	
	private fun getAstPath(
		projectPath: PathWrapper,
		buildPath: PathWrapper,
		sourcePath: PathWrapper,
	): PathWrapper {
		val path = sourcePath.absolutePath.removePrefix(projectPath.absolutePath).removeSuffix(".pzl")
		return PathWrapper(buildPath, "$path.json")
	}
}