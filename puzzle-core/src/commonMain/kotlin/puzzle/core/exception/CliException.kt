package puzzle.core.exception

class CliException(message: String) : PzlException(message)

fun cliError(message: String): Nothing {
	throw CliException(message)
}