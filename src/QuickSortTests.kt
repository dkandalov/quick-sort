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
            it.quickSorted() shouldEqual listOf(1, 2, 3)
        }
    }

    @Test fun `sort list of 4 elements`() {
        listOf(1, 2, 3, 4).permutations().forEach {
            it.quickSorted() shouldEqual listOf(1, 2, 3, 4)
        }
    }

    @Test fun `sort random list`() {
        val list = Random.listOfInts(
            sizeRange = 0..100,
            valuesRange = 0..100
        )
        list.printed().quickSorted().isSorted() shouldEqual true
    }

    @Test fun `sort already sorted list`() {
        (0..100_000).toList().quickSorted().isSorted() shouldEqual true
    }

    private fun List<Int>.isSorted() =
        windowed(size = 2).all { it[0] <= it[1] }
}

fun <E: Comparable<E>> MutableList<E>.quickSort(
    from: Int = 0,
    to: Int = size - 1
): MutableList<E> {
    if (from >= to) return this
    val i = hoarePartition(from, to)
    quickSort(from, i)
    quickSort(i + 1, to)
    return this
}

fun <E: Comparable<E>> MutableList<E>.hoarePartition(from: Int, to: Int): Int {
    val pivot = this[from]
    var i = from
    var j = to
    while (true) {
        while (this[i] < pivot) i++
        while (this[j] > pivot) j--
        if (i < j) swap(i++, j--)
        else return j
    }
}

fun <E: Comparable<E>> List<E>.quickSorted(): List<E> {
    return shuffled().toMutableList().quickSort()
}

fun <E: Comparable<E>> List<E>.quickSorted_functional(): List<E> {
    if (size <= 1) return this
    val pivot = first()
    val (left, right) = drop(1).partition { it < pivot }
    return left.quickSorted_functional() + pivot + right.quickSorted_functional()
}

