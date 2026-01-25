package puzzle.context

import kotlin.reflect.KClass

sealed class Context {
	
	abstract val parent: Context
	
	private val attachment = mutableMapOf<KClass<out ContextAttachment>, ContextAttachment>()
	
	fun <T : ContextAttachment> put(value: T) {
		this.attachment[value::class] = value
	}
	
	@Suppress("UNCHECKED_CAST")
	fun <T : ContextAttachment> get(key: KClass<T>): T {
		return attachment[key] as T
	}
}