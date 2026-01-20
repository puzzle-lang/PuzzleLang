package puzzle.core.util

import puzzle.core.cli.debugFeature
import puzzle.core.frontend.model.Context

fun String.toAnsiString(style: AnsiStyle, vararg styles: AnsiStyle): String {
	if (!debugFeature.enableAnsiColor) return this
	val styles = arrayOf(style, *styles).joinToString("")
	return "$styles$this${AnsiStyle.RESET}"
}

fun StringBuilder.beginAnsi(style: AnsiStyle, vararg styles: AnsiStyle) {
	if (!debugFeature.enableAnsiColor) return
	val styles = arrayOf(AnsiStyle.RESET, style, *styles).joinToString("")
	this.append(styles)
}

fun StringBuilder.appendAnsi(style: AnsiStyle, vararg styles: AnsiStyle) {
	if (!debugFeature.enableAnsiColor) return
	val styles = arrayOf(style, *styles).joinToString("")
	this.append(styles)
}

fun StringBuilder.endAnsi() {
	if (!debugFeature.enableAnsiColor) return
	this.append(AnsiStyle.RESET)
}

enum class AnsiStyle(val code: String) {
	
	/* ---------- 重置 ---------- */
	
	RESET("\u001B[0m"),
	
	/* ---------- 文本属性 ---------- */
	
	BOLD("\u001B[1m"),
	DIM("\u001B[2m"),
	ITALIC("\u001B[3m"),
	UNDERLINE("\u001B[4m"),
	BLINK("\u001B[5m"),
	REVERSE("\u001B[7m"),
	HIDDEN("\u001B[8m"),
	STRIKETHROUGH("\u001B[9m"),
	NORMAL_INTENSITY("\u001B[22m"),
	ITALIC_OFF("\u001B[23m"),
	UNDERLINE_OFF("\u001B[24m"),
	BLINK_OFF("\u001B[25m"),
	REVERSE_OFF("\u001B[27m"),
	HIDDEN_OFF("\u001B[28m"),
	STRIKETHROUGH_OFF("\u001B[29m"),
	
	/* ---------- 文本色 ---------- */
	
	BLACK("\u001B[30m"),
	RED("\u001B[31m"),
	GREEN("\u001B[32m"),
	YELLOW("\u001B[33m"),
	BLUE("\u001B[34m"),
	MAGENTA("\u001B[35m"),
	CYAN("\u001B[36m"),
	WHITE("\u001B[37m"),
	BRIGHT_BLACK("\u001B[90m"),
	BRIGHT_RED("\u001B[91m"),
	BRIGHT_GREEN("\u001B[92m"),
	BRIGHT_YELLOW("\u001B[93m"),
	BRIGHT_BLUE("\u001B[94m"),
	BRIGHT_MAGENTA("\u001B[95m"),
	BRIGHT_CYAN("\u001B[96m"),
	BRIGHT_WHITE("\u001B[97m"),
	
	/* ---------- 背景色 ---------- */
	
	BG_BLACK("\u001B[40m"),
	BG_RED("\u001B[41m"),
	BG_GREEN("\u001B[42m"),
	BG_YELLOW("\u001B[43m"),
	BG_BLUE("\u001B[44m"),
	BG_MAGENTA("\u001B[45m"),
	BG_CYAN("\u001B[46m"),
	BG_WHITE("\u001B[47m"),
	BG_BRIGHT_BLACK("\u001B[100m"),
	BG_BRIGHT_RED("\u001B[101m"),
	BG_BRIGHT_GREEN("\u001B[102m"),
	BG_BRIGHT_YELLOW("\u001B[103m"),
	BG_BRIGHT_BLUE("\u001B[104m"),
	BG_BRIGHT_MAGENTA("\u001B[105m"),
	BG_BRIGHT_CYAN("\u001B[106m"),
	BG_BRIGHT_WHITE("\u001B[107m");
	
	override fun toString(): String {
		return this.code
	}
}