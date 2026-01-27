package puzzle.cli

import puzzle.core.exception.PzlException

class CliException(message: String) : PzlException(message)

fun cliError(message: String): Nothing {
	throw CliException(message)
}