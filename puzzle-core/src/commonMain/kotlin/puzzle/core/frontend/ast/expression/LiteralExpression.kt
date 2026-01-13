package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.token.kinds.NumericType
import puzzle.core.frontend.token.kinds.NumberSystem
import puzzle.core.frontend.ast.expression.Expression as PzlExpression

@Serializable
sealed interface LiteralExpression : PzlExpression

@Serializable
class NumberLiteral(
	val value: String,
	val system: NumberSystem,
	val type: NumericType,
	override val location: SourceLocation,
) : LiteralExpression

@Serializable
sealed interface StringLiteral : LiteralExpression {
	
	@Serializable
	class Text(
		val value: String,
		override val location: SourceLocation,
	) : StringLiteral
	
	@Serializable
	class Template(
		val parts: List<Part>,
		override val location: SourceLocation,
	) : StringLiteral {
		
		@Serializable
		sealed interface Part : PzlAstNode {
			
			@Serializable
			class Text(
				val value: String,
				override val location: SourceLocation,
			) : Part
			
			@Serializable
			class Expression(
				val expression: PzlExpression,
				override val location: SourceLocation,
			) : Part
		}
	}
}

@Serializable
class BooleanLiteral(
	val value: Boolean,
	override val location: SourceLocation,
) : LiteralExpression

@Serializable
class CharLiteral(
	val value: String,
	override val location: SourceLocation,
) : LiteralExpression

@Serializable
class NullLiteral(
	override val location: SourceLocation,
) : LiteralExpression