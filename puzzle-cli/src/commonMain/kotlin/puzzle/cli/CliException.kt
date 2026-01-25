package puzzle.cli

import puzzle.base.exception.PzlException

class CliException(message: String) : PzlException(message)

fun cliError(message: String): Nothing {
	throw CliException(message)
}