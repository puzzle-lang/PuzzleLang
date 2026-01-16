package puzzle.core.util

import kotlinx.serialization.json.Json
import puzzle.core.frontend.ast.AstSerializersModule

val json = Json {
	prettyPrint = true
	encodeDefaults = true
	classDiscriminator = "class"
	ignoreUnknownKeys = true
	serializersModule = AstSerializersModule
}