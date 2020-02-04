@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

/**
 * Класс "комплексое число".
 *
 * Общая сложность задания -- лёгкая.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(val re: Double, val im: Double) {

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(0.0, 0.0)

    /**
     * Конструктор из строки вида x+yi
     */
    constructor(s: String) : this(0.0, 0.0)

    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(0.0, 0.0)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(0.0, 0.0)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = Complex(0.0, 0.0)

    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex = Complex(0.0, 0.0)

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex = Complex(0.0, 0.0)

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = true

    /**
     * Преобразование в строку
     */
    override fun toString(): String = ""
}