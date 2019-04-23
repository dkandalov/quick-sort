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

    @Test fun `sort list of 3 elements`() {
        listOf(1, 2, 3).permutations().forEach {
            it.printed().quickSorted() shouldEqual listOf(1, 2, 3)
        }
    }

    @Test fun `sort random list`() {
        val list = Random(seed = 1234).listOfInts(
            sizeRange = 0..1_000_000,
            valuesRange = 0..100
        )
        list.quickSorted().expectToBeSorted()
    }

    @Test fun `partition a list`() {
        mutableListOf(3, 4, 2, 1).let {
            it.hoarePartition() shouldEqual 1
            it shouldEqual listOf(1, 2, 4, 3)
        }
    }

    @Test fun `sort already sorted list`() {
        (0..100_000).toList().quickSorted().expectToBeSorted()
    }

    private fun <E: Comparable<E>> List<E>.expectToBeSorted() =
        windowed(size = 2).all { it[0] <= it[1] } shouldEqual true
}

fun <E: Comparable<E>> List<E>.quickSorted(): List<E> {
    return shuffled().toMutableList().quickSort()
}

fun <E: Comparable<E>> MutableList<E>.quickSort(
    from: Int = 0,
    to: Int = size - 1
): List<E> {
    if (to <= from) return this
    val i = this.hoarePartition(from, to)
    quickSort(from, i)
    quickSort(i + 1, to)
    return this
}

fun <E: Comparable<E>> MutableList<E>.hoarePartition(
    from: Int = 0,
    to: Int = size - 1
): Int {
    val pivot = this[from]
    var left = from - 1
    var right = to + 1
    while (true) {
        do right-- while (this[right] > pivot)
        do left++ while (this[left] < pivot)
        if (left < right) swap(left, right)
        else return right
    }
}

fun <E: Comparable<E>> List<E>.quickSorted_functional(): List<E> {
    if (size <= 1) return this
    val pivot = first()
    val (left, right) = drop(1).partition { it < pivot }
    return left.quickSorted_functional() + pivot + right.quickSorted_functional()
}
