package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import puzzle.core.frontend.ast.AstDebugWriter
import puzzle.core.frontend.processFrontend
import puzzle.core.util.PathWrapper
import puzzle.core.util.getCurrentMemoryUsage
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

fun main(args: Array<out String>) {
	val command = args.firstOrNull() ?: return help()
	when (command) {
		"build" -> {
			val projectPath = args.drop(1).firstOrNull()
				?: return println("缺少项目路径, 使用 puzzle help 查看使用手册")
			build(PathWrapper(projectPath))
		}
		
		"help" -> help()
		"version" -> version()
		else -> unknown()
	}
}

private fun build(projectPath: PathWrapper) = runBlocking(Dispatchers.Default) {
	val value = measureTimedValue { processFrontend(projectPath) }
	println("执行用时: ${value.duration}")
	val usage = getCurrentMemoryUsage()
	println("内存使用: $usage")
	val writeDuration = measureTime { AstDebugWriter.write(projectPath, value.value) }
	println("AST 已保存: $writeDuration")
}

private fun help() {
	val message = """
        ╭───────────────────────────────┬───────────────────────────────────╮
        │ Puzzle CLI                    │ Usage Information                 │
        ├───────────────────────────────┼───────────────────────────────────┤
        │ puzzle build <project-path>   │ Build the Puzzle project          │
        │ puzzle version                │ Show Puzzle version information   │
        │ puzzle help                   │ Show this help message            │
        ╰───────────────────────────────┴───────────────────────────────────╯
    """.trimIndent()
	println(message)
}

private fun version() {
	val message = """
        ╭───────────────────────────┬──────────────╮
        │ Puzzle CLI                │ v0.1.3-dev   │
        ├───────────────────────────┼──────────────┤
        │ kotlin                    │ v2.3.0       │
        │ kotlinx-coroutines-core   │ v1.10.2      │
        │ kotlinx-serialization     │ v1.10-0-RC   │
        │ kotlinx-io-core           │ v0.8.2       │
        ╰───────────────────────────┴──────────────╯
    """.trimIndent()
	println(message)
}

private fun unknown() {
	println("未知命令, 请使用: puzzle -h 或 puzzle --help 查看使用帮助")
}