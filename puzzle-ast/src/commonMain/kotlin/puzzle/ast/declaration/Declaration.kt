package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode

@Serializable
sealed interface Declaration : PzlAstNode

@Serializable
sealed interface TopLevelAllowedDeclaration : Declaration