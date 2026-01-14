package puzzle.core.util

expect fun getCurrentMemoryUsage(): MemoryUsage

class MemoryUsage(
	val usageBytes: Long,
) {
	
	override fun toString(): String {
		val sb = StringBuilder()
		val gb = (usageBytes / (1024L * 1024L * 1024L)) % 1024L
		if (gb > 0L) {
			sb.append("$gb GB ")
		}
		val mb = (usageBytes / (1024L * 1024L)) % 1024L
		if (mb > 0L) {
			sb.append("$mb MB ")
		}
		val kb = (usageBytes / 1024L) % 1024L
		if (kb > 0L) {
			sb.append("$kb KB ")
		}
		return sb.trim().toString()
	}
}