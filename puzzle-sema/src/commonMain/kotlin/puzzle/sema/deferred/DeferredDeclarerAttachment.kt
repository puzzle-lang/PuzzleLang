package puzzle.sema.deferred

import puzzle.core.context.ContextAttachment
import puzzle.core.context.FileContext

class DeferredDeclarerAttachment internal constructor(
    val values: MutableList<DeferredDeclarer>,
) : ContextAttachment

context(file: FileContext)
fun getDeferredDeclarers(): MutableList<DeferredDeclarer> {
    val attachment = file.getOrPut(DeferredDeclarerAttachment::class) {
        DeferredDeclarerAttachment(mutableListOf())
    }
    return attachment.values
}