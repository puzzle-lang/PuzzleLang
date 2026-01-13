package puzzle.core.frontend.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.PzlAstNode

@Serializable
sealed interface Statement : PzlAstNode