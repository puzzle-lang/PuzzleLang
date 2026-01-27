package puzzle.core.util

actual fun currentMemoryUsage(): MemoryUsage {
	val runtime = Runtime.getRuntime()
	val usageBytes = runtime.totalMemory() - runtime.freeMemory()
	return MemoryUsage(usageBytes)
}