package puzzle.export

import kotlinx.serialization.json.Json

val json = Json {
	prettyPrint = true
	encodeDefaults = true
	classDiscriminator = "class"
	ignoreUnknownKeys = true
	serializersModule = AstSerializersModule
}