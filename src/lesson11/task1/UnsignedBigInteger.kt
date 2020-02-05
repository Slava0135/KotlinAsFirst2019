package lesson11.task1

import java.lang.ArithmeticException
import java.lang.Integer.min
import kotlin.math.max

/**
 * Класс "беззнаковое большое целое число".
 *
 * Общая сложность задания -- очень сложная.
 * Объект класса содержит целое число без знака произвольного размера
 * и поддерживает основные операции над такими числами, а именно:
 * сложение, вычитание (при вычитании большего числа из меньшего бросается исключение),
 * умножение, деление, остаток от деления,
 * преобразование в строку/из строки, преобразование в целое/из целого,
 * сравнение на равенство и неравенство
 */
class UnsignedBigInteger : Comparable<UnsignedBigInteger> {

    private val base = 1000000000
    private val maxDigitSize = 9
    private val data: List<Int>

    /**
     * Конструктор из строки
     */

    constructor(s: String) {
        require(s.matches(Regex("""\d*""")))
        data = s.reversed().chunked(maxDigitSize) { digit: CharSequence -> digit.reversed().toString().toInt() }
    }

    /**
     * Конструктор из целого
     */
    constructor(i: Int) {
        require(i >= 0)
        data = listOf(i)
    }

    private constructor(l: List<Int>) {
        data = l
    }

    private fun operation(other: UnsignedBigInteger, mode: Int): UnsignedBigInteger {
        val summary = MutableList(max(other.data.size, data.size)) { 0 }
        for (i in data.indices) {
            summary[i] = data[i]
        }
        for (i in other.data.indices) {
            summary[i] += mode * other.data[i]
        }
        for (i in 0 until summary.size - 1) {
            if (summary[i] >= base || summary[i] < 0) {
                summary[i] -= mode * base
                summary[i + 1] += mode
            }
        }
        if (mode == 1) {
            if (summary.last() >= base) {
                summary[summary.size - 1] -= base
                summary.add(1)
            }
        } else {
            if (summary.last() == 0 && summary.size > 1) {
                summary.removeAt(summary.size - 1)
            }
        }
        return UnsignedBigInteger(summary)
    }

    /**
     * Сложение
     */
    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger = operation(other, 1)

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */

    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger {
        if (this < other) throw ArithmeticException()
        return operation(other, -1)
    }

    /**
     * Умножение
     */
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger = TODO()

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger = TODO()

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger = TODO()

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?): Boolean =
        (other is UnsignedBigInteger && data == other.data)

    /**
     * Сравнение на больше/меньше (по контракту Comparable.compareTo)
     */
    override fun compareTo(other: UnsignedBigInteger): Int {
        when {
            data.size > other.data.size -> return 1
            data.size < other.data.size -> return -1
            else -> {
                for (i in data.indices) {
                    if (data[i] > other.data[i]) return 1
                    if (data[i] < other.data[i]) return -1
                }
                return 0
            }
        }
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String {
        val lst = mutableListOf<String>()
        for (i in 0 until data.size - 1) {
            val digit = data[i]
            if (digit == 0) {
                lst.add("0".repeat(maxDigitSize))
            } else lst.add(digit.toString())
        }
        lst.add(data.last().toString())
        return lst.joinToString("")
    }

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt() =
        if (data.size > 1) throw ArithmeticException()
        else data.last()
}