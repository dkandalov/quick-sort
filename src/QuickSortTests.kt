import kotlincommon.listOfInts
import kotlincommon.permutations
import kotlincommon.printed
import kotlincommon.swap
import kotlincommon.test.shouldEqual
import org.junit.Test
import kotlin.random.Random


class QuickSortTests {
    @Test fun `trivial examples`() {
        emptyList<Int>().quickSorted() shouldEqual emptyList()
        listOf(1).quickSorted() shouldEqual listOf(1)
    }

    @Test fun `sort list of 2 elements`() {
        listOf(1, 1).quickSorted() shouldEqual listOf(1, 1)
        listOf(1, 2).quickSorted() shouldEqual listOf(1, 2)
        listOf(2, 1).quickSorted() shouldEqual listOf(1, 2)
    }

    @Test fun `sort list of 3 elements`() {
        listOf(1, 2, 3).permutations().forEach {
            it.printed().quickSorted() shouldEqual listOf(1, 2, 3)
        }
    }

    @Test fun `sort random list`() {
        val list = Random.listOfInts(
            sizeRange = 0..100_000,
            valuesRange = 0..100
        )
        list.quickSorted().isSorted() shouldEqual true
    }

    @Test fun `sort already sorted list`() {
        (0..100_000).toList().quickSorted().isSorted() shouldEqual true
    }

    private fun List<Int>.isSorted() =
        windowed(size = 2).all { it[0] <= it[1] }
}

fun <E: Comparable<E>> List<E>.quickSorted(): List<E> {
    return shuffled().toMutableList().quickSort()
}

fun <E: Comparable<E>> MutableList<E>.quickSort(
    from: Int = 0,
    to: Int = size - 1
): MutableList<E> {
    if (from >= to) return this
    val i = hoarePartition(this, from, to)
    quickSort(from, i)
    quickSort(i + 1, to)
    return this
}

fun <E: Comparable<E>> hoarePartition(list: MutableList<E>, from: Int, to: Int): Int {
    val pivot = list[from]
    var left = from
    var right = to
    while (true) {
        while (list[left] < pivot) left++
        while (list[right] > pivot) right--
        if (left < right) list.swap(left++, right--)
        else return right
    }
}

fun <E: Comparable<E>> List<E>.quickSort_functional(): List<E> {
    if (isEmpty()) return this
    val pivot = first()
    val (left, right) = drop(1).partition { it < pivot }
    return left.quickSort_functional() + pivot + right.quickSort_functional()
}
