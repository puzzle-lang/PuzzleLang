package puzzle.driver

import kotlinx.serialization.json.Json
import puzzle.core.context.FileContext
import puzzle.core.context.ModuleContext
import puzzle.core.context.ProjectContext
import puzzle.core.context.RootContext
import puzzle.core.environment.PzlEnvironment
import puzzle.core.io.FilePath
import puzzle.core.io.path
import puzzle.core.util.AnsiStyle
import puzzle.core.util.appendAnsi
import puzzle.core.util.beginAnsi
import puzzle.core.util.endAnsi
import puzzle.config.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

object SourceCollector {
	
	private val json = Json {
		encodeDefaults = true
		explicitNulls = false
	}
	
	private val validNameRegex = "^[a-z][a-z0-9-]*$".toRegex()
	
	private val validVersionRegex = "^\\d+\\.\\d+\\.\\d+$".toRegex()
	
	private val validGroupRegex = "^[a-z][a-z0-9]*(?:\\.[a-z][a-z0-9]*)*$".toRegex()
	
	private lateinit var projectConfigs: Collection<ProjectConfig>
	
	fun collect(projectPath: FilePath) {
		configCheck(projectPath.exists && projectPath.isDirectory) {
			configError("项目不存在", projectPath.name)
		}
		val projectConfigMap = getAllProjectConfigMap(projectPath, isRootProject = true)
		this.projectConfigs = projectConfigMap.values
		RootContext.projects = projectConfigMap.map { (path, config) ->
			getProjectContext(path, config)
		}
		if (PzlEnvironment.enableInfoIgnore) {
			printIgnoreRules()
		}
		RootContext.maxPathLength = calcMaxPathLength()
	}
	
	private fun getAllProjectConfigMap(projectPath: FilePath, isRootProject: Boolean): Map<FilePath, ProjectConfig> {
		return buildMap {
			val config = getProjectConfig(projectPath, isRootProject)
			this[projectPath] = config
			val deps = config.deps?.takeIf { it.isNotEmpty() } ?: return@buildMap
			deps.forEach { dep ->
				var path = path(dep)
				if (!path.exists) {
					path = path(projectPath, dep)
				}
				this += getAllProjectConfigMap(path, isRootProject = false)
			}
		}
	}
	
	private fun getProjectConfig(projectPath: FilePath, isRootProject: Boolean): ProjectConfig {
		val configPath = path(projectPath, "puzzle.json")
		configCheck(configPath.exists && configPath.isFile) {
			configError("项目配置文件不存在", configPath.name)
		}
		val path = configPath.absolutePath
		
		val config = json.decodeFromString<ProjectConfig>(configPath.readText())
		
		val name = config.name ?: configError("项目名未配置", "name", path = path)
		configCheck(name.isNotBlank()) {
			configError("项目名不能为空", "name", path = path)
		}
		configCheck(name.isValidName()) {
			configError("项目名格式错误, 只允许包含 'a'-'z' 'A'-'Z' '-'", "name", path = path)
		}
		
		val version = config.version ?: configError("版本号未配置", "version", path = path)
		configCheck(version.isNotBlank()) {
			configError("版本号不能为空", "version", path = path)
		}
		configCheck(version.isValidVersion()) {
			configError("版本号格式错误, 正确格式: 大版本.小版本.补丁号", "version", version, path)
		}
		
		val modules = config.modules ?: configError("模块列表未配置", "modules", path = path)
		configCheck(modules.isNotEmpty()) {
			configError("模块列表不能为空", "modules", path = path)
		}
		
		if (!isRootProject) return config
		
		val entry = config.entry ?: configError("入口模块未配置", "entry", path = path)
		configCheck(entry.isNotBlank()) {
			configError("入口模块不能为空", "entry", path = path)
		}
		configCheck(entry in modules) {
			configError("入口模块未在模块列表中找到", "entry", entry, path)
		}
		return config
	}
	
	private fun getModuleConfig(modulePath: FilePath, projectConfig: ProjectConfig): ModuleConfig {
		configCheck(modulePath.exists && modulePath.isDirectory) {
			configError("模块不存在", modulePath.name)
		}
		val configPath = path(modulePath, "puzzle.json")
		configCheck(configPath.exists && configPath.isFile) {
			configError("${configPath.name} 模块配置文件不存在")
		}
		val path = configPath.absolutePath
		
		val config = json.decodeFromString<ModuleConfig>(configPath.readText())
		
		val name = config.name ?: configError("模块名未配置", "name", path = path)
		configCheck(name.isNotBlank()) {
			configError("模块名不能为空", "name", path = path)
		}
		configCheck(name.isValidName()) {
			configError("模块名格式错误, 只允许包含 'a'-'z' 'A'-'Z' '-'", "name", name, path)
		}
		
		val version = config.version
		if (version != null) {
			configCheck(version.isNotBlank()) {
				configError("版本号不能为空", "version", path)
			}
			configCheck(version.isValidVersion()) {
				configError("版本号格式错误, 正确格式: 大版本.小版本.补丁号", "version", version, path)
			}
		} else {
			config.version = projectConfig.version
		}
		
		val group = config.group ?: configError("组未配置", "group", path = path)
		configCheck(group.isValidGroup()) {
			configError("组格式错误", "group", group, path)
		}
		
		return config
	}
	
	private fun getProjectContext(path: FilePath, config: ProjectConfig): ProjectContext {
		return ProjectContext().apply {
			this.name = config.name!!
			this.path = path
			this.modules = config.modules!!.map {
				val modulePath = path(path, it)
				val moduleConfig = getModuleConfig(modulePath, config)
				getModuleContext(modulePath, moduleConfig)
			}
		}
	}
	
	context(project: ProjectContext)
	private fun getModuleContext(path: FilePath, config: ModuleConfig): ModuleContext {
		val sourcePath = path(path, "src", "main")
		configCheck(sourcePath.exists && sourcePath.isDirectory) {
			configError("源目录不存在", sourcePath.name)
		}
		val ignoreRules = config.ignore?.toIgnoreRules(path).orEmpty()
		val (fileRules, dirRules) = ignoreRules.partition { it.kind == IgnoreKind.EXACT }
		return ModuleContext().apply {
			this.name = config.name!!
			this.parent = project
			this.files = getAllSourcePaths(sourcePath, fileRules, dirRules).map { path ->
				getFileContext(path)
			}
			this += IgnoreRuleAttachment(ignoreRules)
			val deps = config.deps?.mapIndexed { index, dep ->
				configCheck(" " !in dep) {
					configError("依赖不允许包含空格符", "deps[$index]", dep)
				}
				val groups = dep.split(":")
				val projectName = if (groups.size == 1) project.name else groups.first()
				val projectConfig = projectConfigs.find { it.name == projectName }
					?: configError("依赖不存在", "deps[$index]", dep, path.absolutePath)
				val moduleName = groups.last()
				configCheck(moduleName in projectConfig.modules!!) {
					configError("依赖不存在", "deps[$index]", dep, path.absolutePath)
				}
				Dependence(projectName, moduleName)
			}?.toMutableSet() ?: mutableSetOf()
			this += DependenceAttachment(deps)
		}
	}
	
	context(module: ModuleContext)
	private fun getFileContext(path: FilePath): FileContext {
		return FileContext().apply {
			this.parent = module
			this.name = path.name.removeSuffix(".pzl")
			this.path = path
		}
	}
	
	private fun getAllSourcePaths(sourcePath: FilePath, fileRules: List<IgnoreRule>, dirRules: List<IgnoreRule>): List<FilePath> {
		if (!sourcePath.exists) return emptyList()
		return when {
			sourcePath.isFile && sourcePath.name.endsWith(".pzl") -> if (sourcePath.isIgnoreFile(fileRules)) emptyList() else listOf(sourcePath)
			sourcePath.isDirectory -> {
				val kind = sourcePath.matchIgnoreKind(dirRules)
				when (kind) {
					IgnoreKind.RECURSIVE -> emptyList()
					IgnoreKind.CHILDREN -> {
						sourcePath.list()
							.filter { it.isDirectory }
							.flatMap { getAllSourcePaths(it, fileRules, dirRules) }
					}
					
					else -> {
						sourcePath.list()
							.flatMap { getAllSourcePaths(it, fileRules, dirRules) }
					}
				}
			}
			
			else -> emptyList()
		}
	}
	
	private fun String.isValidName(): Boolean = this matches validNameRegex
	
	private fun String.isValidVersion(): Boolean = this matches validVersionRegex
	
	private fun String.isValidGroup(): Boolean = this matches validGroupRegex
	
	@OptIn(ExperimentalContracts::class)
	private fun configCheck(value: Boolean, lazyError: () -> Nothing) {
		contract {
			returns() implies value
		}
		if (!value) lazyError()
	}
	
	private fun List<String>.toIgnoreRules(modulePath: FilePath): Set<IgnoreRule> {
		val path = modulePath.absolutePath
		return this.mapIndexed { index, ignore ->
			when {
				ignore == "**" -> IgnoreRule(path, IgnoreKind.RECURSIVE, ignore)
				ignore == "*" -> IgnoreRule(path, IgnoreKind.CHILDREN, ignore)
				ignore.endsWith("/**") -> IgnoreRule("$path/${ignore.removeSuffix("/**")}", IgnoreKind.RECURSIVE, ignore)
				ignore.endsWith("/*") -> IgnoreRule("$path/${ignore.removeSuffix("/*")}", IgnoreKind.CHILDREN, ignore)
				ignore != ".pzl" && ignore.endsWith(".pzl") -> IgnoreRule("$path/$ignore", IgnoreKind.EXACT, ignore)
				ignore.isBlank() -> configError("规则不能为空", "ignore[$index]", path = "$path/puzzle.json")
				else -> configError(
					"忽略规则错误, 规则示例: '**', '*', 'src/main/puzzle/*', 'src/main/puzzle/**', 'src/main/puzzle/String.pzl'",
					"ignore[$index]",
					ignore,
					"$path/puzzle.json"
				)
			}
		}.toSet()
	}
	
	private fun FilePath.isIgnoreFile(fileRules: List<IgnoreRule>): Boolean {
		if (fileRules.isEmpty()) return false
		val path = this.absolutePath
		return fileRules.any { it.path == path }
	}
	
	private fun FilePath.matchIgnoreKind(dirRules: List<IgnoreRule>): IgnoreKind? {
		if (dirRules.isEmpty()) return null
		val path = this.absolutePath
		return dirRules.find { it.path == path }?.kind
	}
	
	private fun calcMaxPathLength(): Int {
		return RootContext.projects.maxOfOrNull { project ->
			project.modules.maxOfOrNull { module ->
				module.files.maxOfOrNull {
					it.path!!.absolutePath.length
				} ?: 0
			} ?: 0
		} ?: 0
	}
	
	private fun printIgnoreRules() {
		val message = buildString {
			beginAnsi(AnsiStyle.BRIGHT_WHITE)
			appendLine("忽略规则:")
			val projects = RootContext.projects
			projects.forEachIndexed { projectIndex, project ->
				appendAnsi(AnsiStyle.BRIGHT_CYAN)
				append(if (projectIndex == projects.lastIndex) "└─" else "├─")
				appendAnsi(AnsiStyle.BRIGHT_WHITE)
				appendLine(" ${project.name}")
				val modules = project.modules
				modules.forEachIndexed { moduleIndex, module ->
					appendAnsi(AnsiStyle.BRIGHT_CYAN)
					append(if (projectIndex == projects.lastIndex) " " else "│")
					append(" ".repeat(3))
					append(if (moduleIndex == modules.lastIndex) "└─" else "├─")
					appendAnsi(AnsiStyle.BRIGHT_WHITE)
					appendLine(" ${module.name}")
					val ignoreRules = module[IgnoreRuleAttachment::class].value
					ignoreRules.forEachIndexed { index, ignore ->
						appendAnsi(AnsiStyle.BRIGHT_CYAN)
						append(if (projectIndex == RootContext.projects.lastIndex) " " else "│")
						append(" ".repeat(3))
						append(if (moduleIndex == modules.lastIndex) " " else "│")
						append(" ".repeat(3))
						append(if (index == ignoreRules.size - 1) "└─" else "├─")
						appendAnsi(AnsiStyle.BRIGHT_BLUE)
						appendLine(" ${ignore.raw}")
					}
				}
			}
			endAnsi()
		}
		println(message)
	}
}