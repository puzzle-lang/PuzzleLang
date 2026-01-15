package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import puzzle.core.cli.PathOption
import puzzle.core.cli.findOption
import puzzle.core.cli.parseCliOptions
import puzzle.core.cli.whenEnableInfoProgress
import puzzle.core.exception.PzlException
import puzzle.core.exception.cliError
import puzzle.core.frontend.model.RootContext
import puzzle.core.frontend.processFrontend
import puzzle.core.util.CHINESE_SPACE
import puzzle.core.util.format
import puzzle.core.util.getCurrentMemoryUsage
import kotlin.time.measureTime

fun main(vararg args: String) {
	try {
		runBlocking(Dispatchers.Default) {
			val mode = args.firstOrNull() ?: return@runBlocking help()
			when (mode) {
				"build" -> build(args.drop(1))
				"help" -> help()
				"version" -> version()
				else -> unknown()
			}
		}
	} catch (e: PzlException) {
		println("e: ${e.message}")
	} catch (e: Exception) {
		throw e
	}
}

private suspend fun build(args: List<String>) {
	val root = RootContext()
	val options = parseCliOptions(args)
	root.options = options
	val pathOption = options.findOption<PathOption>() ?: cliError("缺少 --path 选项")
	context(root) {
		val duration = measureTime { processFrontend(pathOption) }
		whenEnableInfoProgress {
			println("执行用时${CHINESE_SPACE.repeat(7)}: ${duration.format()}")
			val usage = getCurrentMemoryUsage()
			println("内存使用 ${"[$usage]".padStart(22)}")
		}
	}
	println("执行完成")
}

private fun help() {
	val message = """
		全部用法:
		puzzle build
		    --path=<project-path>                     * 项目路径
		    --debug-features=<option1,option2,...>      DEBUG 功能选项
		        output-ast-json                         输出 AST json 文件
		        all                                     开启以上全部功能
			
			--infos=<option1,option2,...>               日志信息选项
				progress                                显示程序进度
				ignore                                  显示忽略规则
				file                                    文件分析详情
				all                                     开启以上全部功能
				
		puzzle version                                  查看 Puzzle CLI 以及第三方依赖版本信息
		puzzle help                                     查看 Puzzle CLI 帮助文档
		
		注: * 表示必传参数
    """.trimIndent()
	println(message)
}

private fun version() {
	val message = """
		Puzzle CLI 当前版本: v0.1.3-dev
		
		第三方依赖版本:
			kotlin:                     v2.3.0
			kotlinx-coroutines-core:    v1.10.2
			kotlinx-serialization:      v1.10.0-RC
			kotlinx-io-core:            v0.8.2
    """.trimIndent()
	println(message)
}

private fun unknown() {
	cliError("未知命令, 请使用: puzzle -h 或 puzzle --help 查看使用帮助")
}