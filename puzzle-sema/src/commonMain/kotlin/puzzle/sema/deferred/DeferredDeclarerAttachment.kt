package puzzle.sema.deferred

import puzzle.context.ContextAttachment
import puzzle.context.FileContext

class DeferredDeclarerAttachment internal constructor(
	val value: MutableList<DeferredDeclarer>,
) : ContextAttachment

context(file: FileContext)
fun getDeferredDeclarers(): MutableList<DeferredDeclarer> {
	val attachment = file.getOrPut(DeferredDeclarerAttachment::class) {
		DeferredDeclarerAttachment(mutableListOf())
	}
	return attachment.value
}