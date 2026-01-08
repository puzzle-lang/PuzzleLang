package puzzle.core.frontend.lexer

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.token.PzlToken
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACE
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.MetaKind.EOF

object FileLexerScanner {
	
	context(_: FileContext)
	fun scan(input: CharArray): List<PzlToken> {
		val lexer = PzlLexer(input, position = 0)
		return buildList {
			while (true) {
				val token = lexer.nextToken()
				this += token
				if (token.kind == EOF) break
			}
		}
	}
}

object TemplateExpressionLexerScanner {
	
	context(context: FileContext)
	fun scan(input: CharArray, position: Int): List<PzlToken> {
		val lexer = PzlLexer(input, position)
		return buildList {
			var depth = 1
			while (true) {
				val token = lexer.nextToken()
				if (token.kind == LBRACE) {
					depth++
				} else if (token.kind == RBRACE && --depth == 0) {
					this += PzlToken(EOF, token.location)
					break
				}
				this += token
				if (token.kind == EOF) break
			}
		}
	}
}