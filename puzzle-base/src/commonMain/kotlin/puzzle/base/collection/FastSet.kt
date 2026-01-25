package puzzle.base.collection

class FastSet<T>(
	val elements: Array<T>,
) : Set<T> {
	
	override val size = elements.size
	
	private val hashSet = elements.toHashSet()
	
	override fun isEmpty(): Boolean {
		return elements.isEmpty()
	}
	
	override fun contains(element: T): Boolean {
		return element in hashSet
	}
	
	override fun iterator(): Iterator<T> {
		return elements.iterator()
	}
	
	override fun containsAll(elements: Collection<T>): Boolean {
		return elements.all { it in hashSet }
	}
	
	fun fastForEach(action: (T) -> Unit) {
		val arr = elements
		for (i in arr.indices) {
			action(arr[i])
		}
	}
	
	fun fastForEachIndexed(action: (index: Int, T) -> Unit) {
		val arr = elements
		for (i in arr.indices) {
			action(i, arr[i])
		}
	}
}

inline fun <reified T> fastSetOf(vararg elements: T): FastSet<T> {
	return FastSet(elements.toSet().toTypedArray())
}

inline operator fun <reified T> FastSet<out T>.plus(element: T): FastSet<out T> {
	val elements = buildSet<T> {
		this += this@plus.elements
		this += element
	}
	return FastSet(elements.toTypedArray())
}

inline operator fun <reified T> FastSet<out T>.plus(elements: FastSet<out T>): FastSet<out T> {
	val elements = buildSet<T> {
		this += this@plus.elements
		this += elements
	}
	return FastSet(elements.toTypedArray())
}