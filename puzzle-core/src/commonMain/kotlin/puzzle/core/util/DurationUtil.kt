package puzzle.core.util

import kotlin.time.Duration
import kotlin.time.DurationUnit

fun Duration.format(): String {
	if (this.inWholeSeconds > 0) {
		return this.toString(DurationUnit.SECONDS, 3).padStart(8).padEnd(9)
	}
	return this.toString(DurationUnit.MILLISECONDS, 3).padStart(9)
}