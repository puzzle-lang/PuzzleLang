package puzzle.core.util

import kotlin.time.Duration
import kotlin.time.DurationUnit

fun Duration.format(
	unit: DurationUnit = DurationUnit.MILLISECONDS,
	decimals: Int = 3,
): String = this.toString(unit, decimals)