package puzzle.core.frontend.ast

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import puzzle.core.frontend.token.kinds.AssignmentKind
import puzzle.core.frontend.token.kinds.ModifierKind
import puzzle.core.frontend.token.kinds.OperatorKind
import puzzle.core.frontend.token.kinds.SymbolKind
import puzzle.core.util.PathWrapper
import puzzle.core.util.path

val AstSerializersModule = SerializersModule {
	contextual(DotStringListSerializer)
	contextual(SymbolKindSerializer)
	contextual(OperatorKindSerializer)
	contextual(ModifierKindSerializer)
	contextual(AssignmentKindSerializer)
	contextual(PathWrapperSerializer)
}

private object DotStringListSerializer : KSerializer<List<String>> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		DotStringListSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: List<String>) {
		encoder.encodeString(value.joinToString("."))
	}
	
	override fun deserialize(decoder: Decoder): List<String> {
		return decoder.decodeString().split(".")
	}
}

private object SymbolKindSerializer : KSerializer<SymbolKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		SymbolKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: SymbolKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): SymbolKind {
		val symbol = decoder.decodeString()
		return SymbolKind.kinds.first { it.value == symbol }
	}
}

private object OperatorKindSerializer : KSerializer<OperatorKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		OperatorKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: OperatorKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): OperatorKind {
		val symbol = decoder.decodeString()
		return OperatorKind.kinds.first { it.value == symbol }
	}
}

private object ModifierKindSerializer : KSerializer<ModifierKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		OperatorKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: ModifierKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): ModifierKind {
		val symbol = decoder.decodeString()
		return ModifierKind.availableKinds.first { it.value == symbol }
	}
}

private object AssignmentKindSerializer : KSerializer<AssignmentKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		AssignmentKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: AssignmentKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): AssignmentKind {
		val symbol = decoder.decodeString()
		return AssignmentKind.kinds.first { it.value == symbol }
	}
}

private object PathWrapperSerializer : KSerializer<PathWrapper> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		PathWrapperSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: PathWrapper) {
		encoder.encodeString(value.absolutePath)
	}
	
	override fun deserialize(decoder: Decoder): PathWrapper {
		val path = decoder.decodeString()
		return path(path)
	}
}