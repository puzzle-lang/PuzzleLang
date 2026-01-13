package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import puzzle.core.cli.PathOption
import puzzle.core.cli.parseCLIOptions
import puzzle.core.frontend.processFrontend
import puzzle.core.util.format
import puzzle.core.util.getCurrentMemoryUsage
import kotlin.time.measureTime

fun main(vararg args: String) {
	val mode = args.firstOrNull() ?: return help()
	when (mode) {
		"build" -> build(args.drop(1))
		"help" -> help()
		"version" -> version()
		else -> unknown()
	}
}

private fun build(args: List<String>) = runBlocking(Dispatchers.Default) {
	val options = parseCLIOptions(args)
	val pathOption = options.find { it is PathOption } as? PathOption ?: error("缺少 --path=<path> 选项")
	val duration = measureTime { processFrontend(pathOption) }
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