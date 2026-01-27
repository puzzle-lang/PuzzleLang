package puzzle.core.util

import kotlin.jvm.JvmInline

expect fun currentMemoryUsage(): MemoryUsage

@JvmInline
value class MemoryUsage(
	private val usageBytes: Long,
) {
	
	private companion object {
		
		private const val SIZE = 1024L
	}
	
	override fun toString(): String {
		return buildString {
			val gb = (usageBytes / (SIZE * SIZE * SIZE)) % SIZE
			val mb = (usageBytes / (SIZE * SIZE)) % SIZE
			val kb = (usageBytes / SIZE) % SIZE
			if (gb > 0L) append("$gb GB ")
			if (mb > 0L) append("$mb MB ")
			if (kb > 0L) append("$kb KB ")
		}.trim()
	}
}