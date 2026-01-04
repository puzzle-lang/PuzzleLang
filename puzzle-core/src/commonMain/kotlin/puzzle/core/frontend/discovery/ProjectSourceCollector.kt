package puzzle.core.frontend.discovery

import kotlinx.serialization.json.Json
import puzzle.core.util.PathWrapper

object ProjectSourceCollector {
	
	private val json = Json {
		encodeDefaults = true
		explicitNulls = false
	}
	
	private val validNameRegex = "^[a-z][a-z0-9-]*$".toRegex()
	
	private val validVersionRegex = "^\\d+\\.\\d+\\.\\d+$".toRegex()
	
	private val validGroupRegex = "^[a-z][a-z0-9]*(?:\\.[a-z][a-z0-9]*)*$".toRegex()
	
	fun collect(projectPath: PathWrapper): ProjectSource {
		if (!projectPath.exists() || !projectPath.isDirectory) {
			error("错误: $projectPath 项目不存在")
		}
		val projectConfig = decodeProjectConfig(projectPath)
		val moduleSources = projectConfig.modules.map { module ->
			val modulePath = PathWrapper(projectPath, module)
			if (!modulePath.exists() || !modulePath.isDirectory) {
				error("错误: $modulePath 模块不存在")
			}
			val moduleConfig = decodeModuleConfig(modulePath, projectConfig)
			val sourcePath = PathWrapper(modulePath, "src", "main")
			if (!sourcePath.exists() || !sourcePath.isDirectory) {
				error("错误: $sourcePath 源目录不存在")
			}
			getModuleSourceFiles(moduleConfig.name, sourcePath)
		}
		val maxPathLength = moduleSources.maxOf { module ->
			module.paths.maxOf { it.absolutePath.length }
		}
		return ProjectSource(
			name = projectConfig.name,
			modules = moduleSources,
			maxPathLength = maxPathLength
		)
	}
	
	private fun decodeProjectConfig(projectPath: PathWrapper): ProjectConfig {
		val projectConfigPath = PathWrapper(projectPath, "puzzle.json")
		if (!projectConfigPath.exists() || !projectConfigPath.isFile) {
			error("错误: $projectConfigPath 项目配置文件不存在")
		}
		val projectConfig = json.decodeFromString<ProjectConfig>(projectConfigPath.readText())
		if (projectConfig.name.isEmpty()) {
			error("错误: ${projectConfigPath.absolutePath} 项目名称不能为空")
		}
		if (!projectConfig.name.isValidName()) {
			error("错误: ${projectConfigPath.absolutePath} '${projectConfig.name}' 项目名格式错误，只允许包含小写字母数字和下划线")
		}
		if (projectConfig.version.isEmpty()) {
			error("错误: ${projectConfigPath.absolutePath} '${projectConfig.version}' 版本号不能为空")
		}
		if (!projectConfig.version.isValidVersion()) {
			error("错误: ${projectConfigPath.absolutePath} '${projectConfig.version}' 版本号格式错误，正确格式: 大版本.小版本.补丁号")
		}
		return projectConfig
	}
	
	private fun decodeModuleConfig(modulePath: PathWrapper, projectConfig: ProjectConfig): ModuleConfig {
		val moduleConfigPath = PathWrapper(modulePath, "puzzle.json")
		if (!moduleConfigPath.exists() || !moduleConfigPath.isFile) {
			error("错误: $moduleConfigPath 模块配置文件不存在")
		}
		var moduleConfig = json.decodeFromString<ModuleConfig>(moduleConfigPath.readText())
		if (moduleConfig.name.isEmpty()) {
			error("错误: ${moduleConfigPath.absolutePath} 模块名称不能为空")
		}
		if (!moduleConfig.name.isValidName()) {
			error("错误: ${moduleConfigPath.absolutePath} '${moduleConfig.name}' 模块名格式错误，只允许包含小写字母数字和下划线")
		}
		if (moduleConfig.version.isEmpty()) {
			moduleConfig = moduleConfig.copy(projectConfig.version)
		}
		if (!moduleConfig.version.isValidVersion()) {
			error("错误: ${moduleConfigPath.absolutePath} '${moduleConfig.version}' 版本号格式错误，正确格式: 大版本.小版本.补丁号'")
		}
		if (!moduleConfig.group.isValidGroup()) {
			error("错误: ${moduleConfigPath.absolutePath} '${moduleConfig.group}' 组格式错误")
		}
		return moduleConfig
	}
	
	private fun getModuleSourceFiles(name: String, sourcePath: PathWrapper): ModuleSource {
		val paths = collectAllPzlPaths(sourcePath)
		return ModuleSource(name, paths)
	}
	
	private fun collectAllPzlPaths(path: PathWrapper): List<PathWrapper> {
		if (!path.exists()) return emptyList()
		when {
			path.isFile && path.name.endsWith(".pzl") -> return listOf(path)
			path.isDirectory -> return path.list().flatMap { collectAllPzlPaths(it) }
		}
		return emptyList()
	}
	
	private fun String.isValidName(): Boolean = this matches validNameRegex
	
	private fun String.isValidVersion(): Boolean = this matches validVersionRegex
	
	private fun String.isValidGroup(): Boolean = this matches validGroupRegex
	
	private fun ModuleConfig.copy(version: String): ModuleConfig {
		return ModuleConfig(this.name, version, this.group)
	}
}