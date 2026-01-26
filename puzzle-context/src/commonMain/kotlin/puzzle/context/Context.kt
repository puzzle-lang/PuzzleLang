package puzzle.context

import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
sealed class Context {
	
	abstract val parent: Context
	
	private val attachment = mutableMapOf<KClass<out ContextAttachment<*>>, ContextAttachment<*>>()
	
	operator fun <T : ContextAttachment> plusAssign(value: T) {
		this.attachment[value::class] = value
	}
	
	operator fun <T : ContextAttachment> get(key: KClass<T>): T {
		return attachment[key] as T
	}
	
	fun <T : ContextAttachment> getOrPut(key: KClass<T>, defaultValue: () -> T): T {
		return attachment.getOrPut(key, defaultValue) as T
	}
}

context(context: Context)
inline fun <reified CTX : Context> findContext(): CTX {
	var context = context
	while (context !is CTX) {
		context = context.parent
	}
	return context
}