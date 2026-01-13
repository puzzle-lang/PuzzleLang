package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.PzlAstNode

@Serializable
sealed interface Declaration : PzlAstNode

@Serializable
sealed interface TopLevelAllowedDeclaration : Declaration