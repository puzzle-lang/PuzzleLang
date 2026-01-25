package puzzle.cli

object CliMessages {
	
	fun help() {
		val message = """
		全部用法:
		puzzle build
		    --path=<project-path>                     * 项目路径
			
		    --debug-features=<option1,option2,...>      DEBUG 功能选项
		        output-ast-json                         开启输出 AST json 文件
				ansi-color                              开启终端 Ansi 颜色
				error-stack                             开启错误堆栈信息
		        all                                     开启以上功能
				none                                    关闭以上功能          [默认]
			
			--infos=<option1,option2,...>               日志信息选项
				progress                                开启显示程序进度
				ignore                                  开启显示忽略规则
				file                                    开启显示文件分析详情
				all                                     开启以上功能
				none                                    关闭以上功能          [默认]
				
		puzzle version                                  查看 Puzzle CLI 以及第三方依赖版本信息
		
		puzzle help                                     查看 Puzzle CLI 帮助文档
		
		注: * 表示必传参数
    """.trimIndent()
		println(message)
	}
	
	fun version() {
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
	
	fun unknown() {
		cliError("未知命令, 请使用: puzzle -h 或 puzzle --help 查看使用帮助")
	}
}