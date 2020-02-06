package lesson11.task1

import java.lang.ArithmeticException
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
        require(s.matches(Regex("""\d+""")))
        data = s.reversed().chunked(maxDigitSize) { digit: CharSequence -> digit.reversed().toString().toInt() }
    }

    /**
     * Конструктор из целого
     */
    constructor(i: Int) {
        require(i >= 0)
        data =
            i.toString().reversed().chunked(maxDigitSize) { digit: CharSequence -> digit.reversed().toString().toInt() }
    }

    private constructor(l: List<Int>) {
        data = l
    }

    private fun operation(other: UnsignedBigInteger, mode: Int): UnsignedBigInteger {
        val summary = MutableList(max(other.data.size, data.size)) { 0 } //mode = 1 is add and mode = -1 is subtract
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
            while (summary.last() == 0 && summary.size > 1) {
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
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        var result = UnsignedBigInteger(0)
        for ((count, digit) in data.withIndex()) {
            val part = other.data.map { digit.toLong() * it }.toMutableList()
            for (i in 0 until part.size - 1) {
                if (part[i] >= base) {
                    part[i + 1] += part[i] / base
                    part[i] = part[i] % base
                }
            }
            if (part.last() >= base) {
                part.add(part.last() / base)
                part[part.size - 2] = part[part.size - 2] % base
            }
            for (i in 0 until count) {
                part.add(0, 0)
            }
            result += UnsignedBigInteger(part.map { it.toInt() })
        }
        return result
    }

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger {
        if (this < other) return UnsignedBigInteger(0)
        var num = this
        val result = mutableListOf<Int>()
        var digitNum = this.data.size - other.data.size
        while (num > other) {
            var upBorder = base - 1
            var downBorder = 0
            val digits = other.data.toMutableList()
            for (i in 0 until digitNum) {
                digits.add(0, 0)
            }
            val digit = UnsignedBigInteger(digits)
            while (upBorder - downBorder > 1) {
                val middle = (upBorder + downBorder) / 2
                val possibleMultiplier = digit * UnsignedBigInteger(middle)
                if (possibleMultiplier > num) {
                    upBorder = middle - 1
                } else {
                    downBorder = middle
                }
            }
            if (digit * UnsignedBigInteger(upBorder) <= num) {
                downBorder = upBorder
            }
            result.add(downBorder)
            num -= digit * UnsignedBigInteger(downBorder)
            digitNum--
        }
        return UnsignedBigInteger(result.reversed())
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger = this - (this / other) * other

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
                for (i in data.indices.reversed()) {
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
        return lst.reversed().joinToString("")
    }

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt(): Int {
        if (data.size > 2) throw ArithmeticException()
        return if (data.size == 1) data[0]
        else {
            val sum = data[0] + data[1] * base
            if (sum > Int.MAX_VALUE) throw ArithmeticException()
            else sum
        }

    }

    override fun hashCode(): Int = data.hashCode()
}