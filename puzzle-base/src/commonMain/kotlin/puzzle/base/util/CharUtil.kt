package puzzle.base.util

const val CHINESE_SPACE = "\u3000"

private val hexChars = "0123456789ABCDEFabcdef".toSet()

fun String.isHex(): Boolean {
	return this.all { it.isHex() }
}

fun Char.isHex(): Boolean = this in hexChars

private val decimalChars = "0123456789".toSet()

fun String.isDecimal(): Boolean {
	return this.all { it.isDecimal() }
}

fun Char.isDecimal(): Boolean = this in decimalChars

private val binaryChars = "01".toSet()

fun String.isBinary(): Boolean {
	return this.all { it.isBinary() }
}

fun Char.isBinary(): Boolean = this in binaryChars

fun CharArray.safeString(start: Int, length: Int): String? {
	if (start < 0 || length <= 0 || start >= size) return null
	val end = minOf(start + length, size)
	return this.concatToString(start, end)
}