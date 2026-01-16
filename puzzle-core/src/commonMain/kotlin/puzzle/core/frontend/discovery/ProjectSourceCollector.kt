@file:OptIn(ExperimentalContracts::class)

package puzzle.core.frontend.discovery

import kotlinx.serialization.json.Json
import puzzle.core.cli.whenEnableInfoIgnore
import puzzle.core.exception.configError
import puzzle.core.frontend.model.RootContext
import puzzle.core.util.PathWrapper
import puzzle.core.util.path
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

object ProjectSourceCollector {
	
	private val json = Json {
		encodeDefaults = true
		explicitNulls = false
	}
	
	private val validNameRegex = "^[a-z][a-z0-9-]*$".toRegex()
	
	private val validVersionRegex = "^\\d+\\.\\d+\\.\\d+$".toRegex()
	
	private val validGroupRegex = "^[a-z][a-z0-9]*(?:\\.[a-z][a-z0-9]*)*$".toRegex()
	
	context(_: RootContext)
	fun collect(projectPath: PathWrapper): RootSource {
		configCheck(projectPath.exists() && projectPath.isDirectory) {
			configError("项目不存在", projectPath.name)
		}
		val projectConfig = decodeProjectConfig(projectPath, isRootProject = true)
		val projectConfigMap = mapOf(projectPath to projectConfig) + decodeDepProjectConfigs(projectPath, projectConfig.deps)
		
		val projectSources = projectConfigMap.map { (path, projectConfig) ->
			val modules = projectConfig.modules!!
			val moduleSources = modules.map { module ->
				val modulePath = path(path, module)
				configCheck(modulePath.exists() && modulePath.isDirectory) {
					configError("模块不存在", modulePath.name)
				}
				val moduleConfig = decodeModuleConfig(modulePath, projectConfig)
				val ignores = moduleConfig.ignore ?: emptyList()
				getModuleSourceFiles(moduleConfig.name!!, modulePath, ignores)
			}
			ProjectSource(
				name = projectConfig.name!!,
				path = path,
				moduleSources = moduleSources,
			)
		}
		whenEnableInfoIgnore {
			projectSources.printIgnoreRules()
		}
		
		val maxPathLength = projectSources.maxOfOrNull { project ->
			project.moduleSources.maxOfOrNull { module ->
				module.sourcePaths.maxOfOrNull {
					it.absolutePath.length
				} ?: 0
			} ?: 0
		} ?: 0
		
		return RootSource(
			projectSources = projectSources,
			maxPathLength = maxPathLength
		)
	}
	
	private fun decodeProjectConfig(projectPath: PathWrapper, isRootProject: Boolean): ProjectConfig {
		val projectConfigPath = path(projectPath, "puzzle.json")
		configCheck(projectConfigPath.exists() && projectConfigPath.isFile) {
			configError("项目配置文件不存在", projectConfigPath.name)
		}
		val path = projectConfigPath.absolutePath
		
		val projectConfig = json.decodeFromString<ProjectConfig>(projectConfigPath.readText())
		
		val name = projectConfig.name ?: configError("项目名未配置", "name", path = path)
		configCheck(name.isNotBlank()) {
			configError("项目名不能为空", "name", path = path)
		}
		configCheck(name.isValidName()) {
			configError("项目名格式错误, 只允许包含 'a'-'z' 'A'-'Z' '-'", "name", path = path)
		}
		
		val version = projectConfig.version ?: configError("版本号未配置", "version", path = path)
		configCheck(version.isNotBlank()) {
			configError("版本号不能为空", "version", path = path)
		}
		configCheck(version.isValidVersion()) {
			configError("版本号格式错误, 正确格式: 大版本.小版本.补丁号", "version", version, path)
		}
		
		val modules = projectConfig.modules ?: configError("模块列表未配置", "modules", path = path)
		configCheck(modules.isNotEmpty()) {
			configError("模块列表不能为空", "modules", path = path)
		}
		
		if (!isRootProject) return projectConfig
		
		val entry = projectConfig.entry ?: configError("入口模块未配置", "entry", path = path)
		configCheck(entry.isNotBlank()) {
			configError("入口模块不能为空", "entry", path = path)
		}
		configCheck(entry in modules) {
			configError("入口模块未在模块列表中找到", "entry", entry, path)
		}
		return projectConfig
	}
	
	private fun decodeDepProjectConfigs(projectPath: PathWrapper, deps: List<String>?): Map<PathWrapper, ProjectConfig> {
		if (deps.isNullOrEmpty()) return emptyMap()
		return deps.associate { dep ->
			val path = path(projectPath, dep)
			path to decodeProjectConfig(path, isRootProject = false)
		}
	}
	
	private fun decodeModuleConfig(modulePath: PathWrapper, projectConfig: ProjectConfig): ModuleConfig {
		val moduleConfigPath = path(modulePath, "puzzle.json")
		configCheck(moduleConfigPath.exists() && moduleConfigPath.isFile) {
			configError("${moduleConfigPath.name} 模块配置文件不存在")
		}
		val path = moduleConfigPath.absolutePath
		
		val moduleConfig = json.decodeFromString<ModuleConfig>(moduleConfigPath.readText())
		
		val name = moduleConfig.name ?: configError("模块名未配置", "name", path = path)
		configCheck(name.isNotBlank()) {
			configError("模块名不能为空", "name", path = path)
		}
		configCheck(name.isValidName()) {
			configError("模块名格式错误, 只允许包含 'a'-'z' 'A'-'Z' '-'", "name", name, path)
		}
		
		val version = moduleConfig.version
		if (version != null) {
			configCheck(version.isNotBlank()) {
				configError("版本号不能为空", "version", path)
			}
			configCheck(version.isValidVersion()) {
				configError("版本号格式错误, 正确格式: 大版本.小版本.补丁号", "version", version, path)
			}
		} else {
			moduleConfig.version = projectConfig.version
		}
		
		val group = moduleConfig.group ?: configError("组未配置", "group", path = path)
		configCheck(group.isValidGroup()) {
			configError("组格式错误", "group", group, path)
		}
		
		return moduleConfig
	}
	
	private fun getModuleSourceFiles(name: String, modulePath: PathWrapper, ignores: List<String>): ModuleSource {
		val ignoreRules = ignores.toIgnoreRules(modulePath)
		val (fileRules, dirRules) = ignoreRules.partition { it.kind == IgnoreKind.EXACT }
		val sourcePath = path(modulePath, "src", "main")
		configCheck(sourcePath.exists() && sourcePath.isDirectory) {
			configError("源目录不存在", sourcePath.name)
		}
		val sourcePaths = collectAllPzlPaths(sourcePath, fileRules, dirRules)
		return ModuleSource(name, modulePath, sourcePaths, ignores)
	}
	
	private fun collectAllPzlPaths(path: PathWrapper, fileRules: List<IgnoreRule>, dirRules: List<IgnoreRule>): List<PathWrapper> {
		if (!path.exists()) return emptyList()
		return when {
			path.isFile && path.name.endsWith(".pzl") -> if (path.isIgnoreFile(fileRules)) emptyList() else listOf(path)
			
			path.isDirectory -> {
				val kind = path.matchIgnoreKind(dirRules)
				if (kind == IgnoreKind.RECURSIVE) return emptyList()
				path.list().let {
					if (kind == null) it else it.filter { path -> path.isDirectory }
				}.flatMap { collectAllPzlPaths(it, fileRules, dirRules) }
			}
			
			else -> emptyList()
		}
	}
	
	private fun String.isValidName(): Boolean = this matches validNameRegex
	
	private fun String.isValidVersion(): Boolean = this matches validVersionRegex
	
	private fun String.isValidGroup(): Boolean = this matches validGroupRegex
	
	private fun configCheck(value: Boolean, lazyError: () -> Nothing) {
		contract {
			returns() implies value
		}
		if (!value) lazyError()
	}
	
	private fun List<String>.toIgnoreRules(modulePath: PathWrapper): List<IgnoreRule> {
		val path = modulePath.absolutePath
		return this.mapIndexed { index, ignore ->
			when {
				ignore == "**" -> IgnoreRule(path, IgnoreKind.RECURSIVE)
				ignore == "*" -> IgnoreRule(path, IgnoreKind.CHILDREN)
				ignore.endsWith("/**") -> IgnoreRule("$path/${ignore.removeSuffix("/**")}", IgnoreKind.RECURSIVE)
				ignore.endsWith("/*") -> IgnoreRule("$path/${ignore.removeSuffix("/*")}", IgnoreKind.CHILDREN)
				ignore != ".pzl" && ignore.endsWith(".pzl") -> IgnoreRule("$path/$ignore", IgnoreKind.EXACT)
				ignore.isBlank() -> configError("规则不能为空", "ignore[$index]", path = "$path/puzzle.json")
				else -> configError(
					"忽略规则错误, 规则示例: '**', '*', 'src/main/puzzle/*', 'src/main/puzzle/**', 'src/main/puzzle/String.pzl'",
					"ignore[$index]",
					ignore,
					"$path/puzzle.json"
				)
			}
		}.distinct()
	}
	
	private fun PathWrapper.isIgnoreFile(fileRules: List<IgnoreRule>): Boolean {
		if (fileRules.isEmpty()) return false
		val path = this.absolutePath
		return fileRules.any { it.path == path }
	}
	
	private fun PathWrapper.matchIgnoreKind(dirRules: List<IgnoreRule>): IgnoreKind? {
		if (dirRules.isEmpty()) return null
		val path = this.absolutePath
		return dirRules.find { it.path == path }?.kind
	}
	
	private fun List<ProjectSource>.printIgnoreRules() {
		val message = buildString {
			appendLine("忽略规则")
			this@printIgnoreRules.forEachIndexed { projectIndex, project ->
				append(if (projectIndex == this@printIgnoreRules.lastIndex) "└─" else "├─")
				appendLine(" ${project.name}")
				project.moduleSources.forEachIndexed { moduleIndex, module ->
					append(if (projectIndex == this@printIgnoreRules.lastIndex) " " else "│")
					append(" ".repeat(3))
					append(if (moduleIndex == project.moduleSources.lastIndex) "└─" else "├─")
					appendLine(" ${module.name}")
					module.ignore.forEachIndexed { index, ignore ->
						append(if (projectIndex == this@printIgnoreRules.lastIndex) " " else "│")
						append(" ".repeat(3))
						append(if (moduleIndex == project.moduleSources.lastIndex) " " else "│")
						append(" ".repeat(3))
						append(if (index == module.ignore.lastIndex) "└─" else "├─")
						appendLine(" $ignore")
					}
				}
			}
		}
		println(message)
	}
}