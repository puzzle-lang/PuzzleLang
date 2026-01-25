package puzzle.base.util

import kotlinx.cinterop.*
import platform.darwin.*

@OptIn(ExperimentalForeignApi::class)
actual fun currentMemoryUsage(): MemoryUsage {
	val usageBytes = memScoped {
		val info = alloc<task_basic_info>()
		val size = alloc<mach_msg_type_number_tVar>()
		size.value = sizeOf<task_basic_info>().toUInt() / 4u
		val kerr = task_info(
			mach_task_self_,
			TASK_BASIC_INFO.toUInt(),
			info.ptr.reinterpret(),
			size.ptr
		)
		if (kerr == KERN_SUCCESS) {
			info.resident_size.toLong()
		} else -1L
	}
	return MemoryUsage(usageBytes)
}