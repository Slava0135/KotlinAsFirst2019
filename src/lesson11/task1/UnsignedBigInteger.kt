package lesson11.task1

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

    val data = mutableListOf(0)

    /**
     * Конструктор из ничего
     */
    constructor() {
    }

    /**
     * Конструктор из строки
     */

    val data = mutableListOf<Int>()

    constructor(s: String) {
    }

    /**
     * Конструктор из целого
     */
    constructor(i: Int) {
    }

    /**
     * Сложение
     */
    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger = UnsignedBigInteger()

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */
    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger = UnsignedBigInteger()

    /**
     * Умножение
     */
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger = UnsignedBigInteger()

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger = UnsignedBigInteger()

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger = UnsignedBigInteger()

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?): Boolean = true

    /**
     * Сравнение на больше/меньше (по контракту Comparable.compareTo)
     */
    override fun compareTo(other: UnsignedBigInteger): Int = 0

    /**
     * Преобразование в строку
     */
    override fun toString(): String = ""

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt(): Int = 0

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}