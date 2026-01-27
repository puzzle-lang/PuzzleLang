package puzzle.cli

import kotlinx.coroutines.runBlocking
import puzzle.core.exception.PzlException
import puzzle.diagnostic.source.PzlExceptionPrinter
import puzzle.driver.CompilerDriver

fun main(vararg args: String) {
	try {
		dispatchCliCommand(args)
	} catch (e: PzlException) {
		PzlExceptionPrinter.print(e)
	} catch (e: Throwable) {
		throw e
	}
}

private fun dispatchCliCommand(args: Array<out String>) = runBlocking {
	val command = args.firstOrNull()
		?: return@runBlocking CliMessages.help()
	when (command) {
		"build" -> {
			val options = args.drop(1)
			parseCliOptions(options)
			CompilerDriver.build()
		}
		
		"help" -> CliMessages.help()
		"version" -> CliMessages.version()
		else -> CliMessages.unknown()
	}
}