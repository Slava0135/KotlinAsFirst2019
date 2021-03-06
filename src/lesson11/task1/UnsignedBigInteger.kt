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

    private val base = 1_000_000_000
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
        data = if (i > base) {
            listOf(i % base, i / base)
        } else listOf(i)
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
        for (i in 0 until summary.size - 1) {
            if (summary[i] >= base) {
                summary[i] -= base
                summary[i + 1]++
            }
        }
        if (summary.last() >= base) {
            summary[summary.size - 1] -= base
            summary.add(1)
        }
        return UnsignedBigInteger(summary)
    }

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */
    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger {
        if (this < other) throw ArithmeticException()
        val summary = data.toMutableList()
        for (i in other.data.indices) {
            summary[i] -= other.data[i]
        }
        for (i in 0 until summary.size - 1) {
            if (summary[i] < 0) {
                summary[i] += base
                summary[i + 1]--
            }
        }
        while (summary.last() == 0 && summary.size > 1) {
            summary.removeAt(summary.size - 1)
        }
        return UnsignedBigInteger(summary)
    }

    /**
     * Умножение
     */
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        if (this == UnsignedBigInteger(0) || other == UnsignedBigInteger(0)) return UnsignedBigInteger(0)
        var result = UnsignedBigInteger(0)
        for (digit in data.reversed()) {
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
            result += UnsignedBigInteger(part.map { it.toInt() })
            val resultData = result.data.toMutableList()
            resultData.add(0, 0)
            result = UnsignedBigInteger(resultData)
        }
        val resultData = result.data.toMutableList()
        resultData.removeAt(0)
        result = UnsignedBigInteger(resultData)
        return result
    }

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other == UnsignedBigInteger(0)) throw ArithmeticException()
        if (this < other) return UnsignedBigInteger(0)
        var num = this
        val summary = mutableListOf<Int>()
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
            summary.add(downBorder)
            num -= digit * UnsignedBigInteger(downBorder)
            digitNum--
        }
        summary.reverse()
        while (summary.last() == 0 && summary.size > 1) {
            summary.removeAt(summary.size - 1)
        }
        return UnsignedBigInteger(summary)
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
            val sum = data[0] + data[1].toLong() * base
            if (sum > Int.MAX_VALUE) throw ArithmeticException()
            else sum.toInt()
        }
    }

    override fun hashCode(): Int = data.hashCode()
}