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
        val list = Random.listOfInts(
            sizeRange = 0..100,
            valuesRange = 0..100
        )
        list.quickSorted().expectToBeSorted()
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
): MutableList<E> {
    if (to <= from) return this
    val i = hoarePartition(this, from, to)
    quickSort(from, i)
    quickSort(i + 1, to)
    return this
}

fun <E: Comparable<E>> hoarePartition(
    list: MutableList<E>,
    from: Int,
    to: Int
): Int {
    val pivot = list[from]
    var left = from - 1
    var right = to + 1
    while (true) {
        do right-- while (list[right] > pivot)
        do left++ while (list[left] < pivot)
        if (left < right) list.swap(left, right)
        else return right
    }
}

fun <E: Comparable<E>> List<E>.quickSorted_functional(): List<E> {
    if (size <= 1) return this
    val pivot = first()
    val (left, right) = drop(1).partition { it < pivot }
    return left.quickSorted_functional() + pivot + right.quickSorted_functional()
}
