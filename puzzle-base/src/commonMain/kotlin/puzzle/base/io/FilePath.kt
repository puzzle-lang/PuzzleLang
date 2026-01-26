package puzzle.base.io

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString

fun path(path: String): FilePath {
	return FilePath(Path(path))
}

fun path(base: FilePath, vararg parts: String): FilePath {
	return FilePath(Path(base.path, *parts))
}

class FilePath internal constructor(
	internal val path: Path,
) {
	
	val name = path.name
	
	val absolutePath by lazy {
		SystemFileSystem.resolve(path).toString()
	}
	
	val parent by lazy {
		path.parent?.let { Path(it) }
	}
	
	private val metadata = SystemFileSystem.metadataOrNull(path)
	
	val isFile by lazy { metadata?.isRegularFile ?: false }
	
	val isDirectory by lazy { metadata?.isDirectory ?: false }
	
	val exists by lazy { SystemFileSystem.exists(path) }
	
	fun readText(): String {
		return SystemFileSystem.source(path)
			.buffered()
			.use { it.readString() }
	}
	
	fun writeText(text: String, append: Boolean = false) {
		SystemFileSystem.sink(path, append)
			.buffered()
			.use { it.writeString(text) }
	}
	
	fun createDirectories(mustCreate: Boolean = false) {
		SystemFileSystem.createDirectories(path, mustCreate)
	}
	
	fun list(): List<FilePath> {
		return SystemFileSystem.list(path).map {
			FilePath(it)
		}
	}
	
	fun delete() {
		if (!exists) return
		if (isFile) SystemFileSystem.delete(path)
		if (isDirectory) {
			this.list().forEach {
				it.delete()
			}
		}
	}
}