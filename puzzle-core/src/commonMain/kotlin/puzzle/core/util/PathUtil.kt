package puzzle.core.util

import kotlinx.io.InternalIoApi
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString

fun path(path: String): PathWrapper {
	return PathWrapper.of(path)
}

fun path(base: PathWrapper, vararg parts: String): PathWrapper {
	return PathWrapper.of(base, *parts)
}

class PathWrapper private constructor(
	private val path: Path,
) {
	
	companion object {
		
		fun of(path: String): PathWrapper {
			return PathWrapper(Path(path))
		}
		
		fun of(base: PathWrapper, vararg parts: String): PathWrapper {
			return PathWrapper(Path(base.path, *parts))
		}
	}
	
	val absolutePath by lazy {
		SystemFileSystem.resolve(path).toString()
	}
	
	val parent by lazy {
		path.parent?.let { PathWrapper(it) }
	}
	
	private val metadata by lazy {
		SystemFileSystem.metadataOrNull(path)
	}
	
	val isFile: Boolean by lazy { metadata?.isRegularFile ?: false }
	
	val isDirectory: Boolean by lazy { metadata?.isDirectory ?: false }
	
	val name = path.name
	
	fun readText(): String {
		return SystemFileSystem.source(path)
			.buffered()
			.use { it.readString() }
	}
	
	@OptIn(InternalIoApi::class)
	fun writeText(text: String, append: Boolean = false) {
		SystemFileSystem.sink(path, append)
			.buffered()
			.use { it.writeString(text) }
	}
	
	fun createDirectories(mustCreate: Boolean = false) {
		SystemFileSystem.createDirectories(path, mustCreate)
	}
	
	fun exists(): Boolean {
		return SystemFileSystem.exists(path)
	}
	
	fun list(): List<PathWrapper> {
		return SystemFileSystem.list(path).map { PathWrapper(it) }
	}
	
	fun deleteAll() {
		if (!exists()) return
		if (isFile) SystemFileSystem.delete(path)
		if (!isDirectory) return
		this.list().forEach { it.deleteAll() }
		SystemFileSystem.delete(path)
	}
}