package puzzle.driver

import puzzle.base.environment.PzlEnvironment
import puzzle.base.util.*
import kotlin.time.measureTime

object CompilerDriver {
	
	suspend fun build() {
		val projectPath = PzlEnvironment.projectPath!!
		val duration = measureTime {
		
		}
		if (PzlEnvironment.enableInfoProgress) {
			println("执行用时${CHINESE_SPACE.repeat(7)}: ${duration.format()}")
			val usage = currentMemoryUsage()
			println("内存使用 ${"[$usage]".padStart(22).toAnsiString(AnsiStyle.BRIGHT_BLUE)}")
		}
	}
	
//	private suspend fun
}

context(a: Int)
fun a() {

}