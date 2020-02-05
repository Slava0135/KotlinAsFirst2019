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

    /**
     * Сложение
     */
    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger {
        val summary = MutableList(max(other.data.size, data.size)) { 0 }
        for (i in data.indices) {
            summary[i] = data[i]
        }
        for (i in other.data.indices) {
            summary[i] += other.data[i]
        }
        return UnsignedBigInteger(summary)
    }

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */

    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger = TODO()

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
                    if (data[i] > other.data[i]) return -1
                }
                return 0
            }
        }
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String = data.joinToString("")

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt() =
        if (data.size > 1) throw ArithmeticException()
        else data.last()
}