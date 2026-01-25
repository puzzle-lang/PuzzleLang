package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode

@Serializable
sealed interface Statement : PzlAstNode