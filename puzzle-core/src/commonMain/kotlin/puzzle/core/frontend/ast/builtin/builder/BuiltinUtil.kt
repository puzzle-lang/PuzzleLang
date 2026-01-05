package puzzle.core.frontend.ast.builtin.builder

import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.argument.TypeArgument
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.ModifierKind

fun getTypeReference(
	type: String,
	isNullable: Boolean,
	typeArguments: List<TypeArgument>,
): TypeReference {
	return TypeReference(
		type = getNamedType(type, typeArguments),
		isNullable = isNullable,
		location = SourceLocation.Builtin
	)
}

fun getNamedType(type: String, typeArguments: List<TypeArgument>): NamedType {
	return NamedType(
		segments = type.split("."),
		location = SourceLocation.Builtin,
		typeArguments = typeArguments,
	)
}

fun getAnnotationCall(type: String): AnnotationCall {
	return AnnotationCall(
		type = NamedType(
			segments = type.split("."),
			location = SourceLocation.Builtin,
		),
		location = SourceLocation.Builtin
	)
}

fun getModifier(modifier: ModifierKind): Modifier {
	return Modifier(
		kind = modifier,
		location = SourceLocation.Builtin,
	)
}