package puzzle.builtin.builder

import puzzle.ast.AnnotationCall
import puzzle.ast.Modifier
import puzzle.ast.argument.TypeArgument
import puzzle.ast.type.NamedType
import puzzle.ast.type.TypeReference
import puzzle.core.location.SourceLocation
import puzzle.token.kinds.ModifierKind

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