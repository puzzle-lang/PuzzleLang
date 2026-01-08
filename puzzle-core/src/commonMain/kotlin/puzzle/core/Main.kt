package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import puzzle.core.frontend.processFrontend
import puzzle.core.util.PathWrapper
import puzzle.core.util.format
import puzzle.core.util.getCurrentMemoryUsage
import puzzle.core.util.path
import kotlin.time.measureTime

fun main(args: Array<out String>) {
	val command = args.firstOrNull() ?: return help()
	when (command) {
		"build" -> {
			val projectPath = args.drop(1).firstOrNull()
				?: return println("缺少项目路径, 使用 puzzle help 查看使用手册")
			build(path(projectPath))
		}
		
		"help" -> help()
		"version" -> version()
		else -> unknown()
	}
}

private fun build(projectPath: PathWrapper) = runBlocking(Dispatchers.Default) {
	val duration = measureTime { processFrontend(projectPath) }
	println("执行用时: ${duration.format()}")
	val usage = getCurrentMemoryUsage()
	println("内存使用: $usage")
}

private fun help() {
	val message = """
        ┌───────────────────────────────┬───────────────────────────────────┐
        │ Puzzle CLI                    │ Usage Information                 │
        ├───────────────────────────────┼───────────────────────────────────┤
        │ puzzle build <project-path>   │ Build the Puzzle project          │
        │ puzzle version                │ Show Puzzle version information   │
        │ puzzle help                   │ Show this help message            │
        └───────────────────────────────┴───────────────────────────────────┘
    """.trimIndent()
	println(message)
}

private fun version() {
	val message = """
        ┌───────────────────────────┬──────────────┐
        │ Puzzle CLI                │ v0.1.3-dev   │
        ├───────────────────────────┼──────────────┤
        │ kotlin                    │ v2.3.0       │
        │ kotlinx-coroutines-core   │ v1.10.2      │
        │ kotlinx-serialization     │ v1.10-0-RC   │
        │ kotlinx-io-core           │ v0.8.2       │
        └───────────────────────────┴──────────────┘
    """.trimIndent()
	println(message)
}

private fun unknown() {
	println("未知命令, 请使用: puzzle -h 或 puzzle --help 查看使用帮助")
}