@file:Suppress("UNUSED_PARAMETER")

package lesson9.task2

import lesson9.task1.Matrix
import lesson9.task1.createMatrix
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs

// Все задачи в этом файле требуют наличия реализации интерфейса "Матрица" в Matrix.kt

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 * При транспонировании строки матрицы становятся столбцами и наоборот:
 *
 * 1 2 3      1 4 6 3
 * 4 5 6  ==> 2 5 5 2
 * 6 5 4      3 6 4 1
 * 3 2 1
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

/**
 * Пример
 *
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    require(!(width != other.width || height != other.height))
    if (width < 1 || height < 1) return this
    val result = createMatrix(height, width, this[0, 0])
    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i, j] = this[i, j] + other[i, j]
        }
    }
    return result
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width
 * натуральными числами от 1 до m*n по спирали,
 * начинающейся в левом верхнем углу и закрученной по часовой стрелке.
 *
 * Пример для height = 3, width = 4:
 *  1  2  3  4
 * 10 11 12  5
 *  9  8  7  6
 */
fun generateSpiral(height: Int, width: Int): Matrix<Int> {
    val matrix = createMatrix(height, width, 1)

    var subHeightUp = 0
    var subWidthRight = 1
    var subHeightDown = 1
    var subWidthLeft = 0

    var count = 1
    var x = 0
    var y = 0
    val condition = { count < height * width }
    while (true) {

        if (!condition()) break
        while (x < width - subWidthRight) {
            matrix[y, x] = count
            x++
            count++
        }
        subHeightUp++

        if (!condition()) break
        while (y < height - subHeightDown) {
            matrix[y, x] = count
            y++
            count++
        }
        subWidthRight++

        if (!condition()) break
        while (x > subWidthLeft) {
            matrix[y, x] = count
            x--
            count++
        }
        subHeightDown++

        if (!condition()) break
        while (y > subHeightUp) {
            matrix[y, x] = count
            y--
            count++
        }
        subWidthLeft++
    }
    if (matrix[y, x] == 1) matrix[y, x] = count
    return matrix
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width следующим образом.
 * Элементам, находящимся на периферии (по периметру матрицы), присвоить значение 1;
 * периметру оставшейся подматрицы – значение 2 и так далее до заполнения всей матрицы.
 *
 * Пример для height = 5, width = 6:
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1
 *  1  2  3  3  2  1
 *  1  2  2  2  2  1
 *  1  1  1  1  1  1
 */
fun generateRectangles(height: Int, width: Int): Matrix<Int> {
    val matrix = createMatrix(height, width, 1)
    for (count in 2..(minOf(height, width) + 1) / 2) {
        for (i in (count - 1) until width - (count - 1))
            matrix[count - 1, i] = count
        for (i in count until height - count) {
            matrix[i, count - 1] = count
            matrix[i, width - count] = count
        }
        for (i in (count - 1) until width - (count - 1))
            matrix[height - count, i] = count
    }
    return matrix
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width диагональной змейкой:
 * в левый верхний угол 1, во вторую от угла диагональ 2 и 3 сверху вниз, в третью 4-6 сверху вниз и так далее.
 *
 * Пример для height = 5, width = 4:
 *  1  2  4  7
 *  3  5  8 11
 *  6  9 12 15
 * 10 13 16 18
 * 14 17 19 20
 */
fun generateSnake(height: Int, width: Int): Matrix<Int> {

    val matrix = createMatrix(height, width, 0)
    var count = 1
    var position = 0

    fun leftCorner(arg: Int) {
        while (position < arg - 1) {
            for (i in 0..position) {
                matrix[i, position - i] = count
                count++
            }
            position++
        }
    }

    fun rightCorner() {
        while (position > 0) {
            for (i in position downTo 1) {
                matrix[height - i, width - (position - i + 1)] = count
                count++
            }
            position--
        }
    }

    if (height > width) {

        leftCorner(width)

        var j = 0
        while (j < height - position) {
            for (i in 0..position) {
                matrix[j + i, width - (i + 1)] = count
                count++
            }
            j++
        }

        rightCorner()

    } else {

        leftCorner(height)

        var j = 0
        while (j < width - position) {
            for (i in position downTo 0) {
                matrix[height - (i + 1), j + i] = count
                count++
            }
            j++
        }

        rightCorner()

    }
    return matrix
}

/**
 * Средняя
 *
 * Содержимое квадратной матрицы matrix (с произвольным содержимым) повернуть на 90 градусов по часовой стрелке.
 * Если height != width, бросить IllegalArgumentException.
 *
 * Пример:    Станет:
 * 1 2 3      7 4 1
 * 4 5 6      8 5 2
 * 7 8 9      9 6 3
 */
fun <E> rotate(matrix: Matrix<E>): Matrix<E> {
    require(matrix.height == matrix.width)
    val side = matrix.height - 1
    val newMatrix = createMatrix(matrix.height, matrix.width, matrix[0, 0])
    for (x in 0..side) {
        for (y in 0..side) {
            newMatrix[x, side - y] = matrix[y, x]
        }
    }
    return newMatrix
}

fun <E> copy(matrix: Matrix<E>): Matrix<E> {
    val newMatrix = createMatrix(matrix.height, matrix.width, matrix[0, 0])
    for (y in 0 until matrix.height) {
        for (x in 0 until matrix.width) {
            newMatrix[y, x] = matrix[y, x]
        }
    }
    return newMatrix
}

/**
 * Сложная
 *
 * Проверить, является ли квадратная целочисленная матрица matrix латинским квадратом.
 * Латинским квадратом называется матрица размером n x n,
 * каждая строка и каждый столбец которой содержат все числа от 1 до n.
 * Если height != width, вернуть false.
 *
 * Пример латинского квадрата 3х3:
 * 2 3 1
 * 1 2 3
 * 3 1 2
 */
fun isLatinSquare(matrix: Matrix<Int>): Boolean {
    if (matrix.height != matrix.width) return false
    val side = matrix.height - 1
    val nums = List(side + 1) { it + 1 }.toSet()
    for (i in 0..side) {
        val row = mutableSetOf<Int>()
        val column = mutableSetOf<Int>()
        for (j in 0..side) {
            row.add(matrix[i, j])
            column.add(matrix[j, i])
        }
        if (row != nums || column != nums) return false
    }
    return true
}

/**
 * Средняя
 *
 * В матрице matrix каждый элемент заменить суммой непосредственно примыкающих к нему
 * элементов по вертикали, горизонтали и диагоналям.
 *
 * Пример для матрицы 4 x 3: (11=2+4+5, 19=1+3+4+5+6, ...)
 * 1 2 3       11 19 13
 * 4 5 6  ===> 19 31 19
 * 6 5 4       19 31 19
 * 3 2 1       13 19 11
 *
 * Поскольку в матрице 1 х 1 примыкающие элементы отсутствуют,
 * для неё следует вернуть как результат нулевую матрицу:
 *
 * 42 ===> 0
 */
fun sumNeighbours(matrix: Matrix<Int>): Matrix<Int> {
    val allAround =
        listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0), Pair(-1, -1), Pair(-1, 1), Pair(1, 1), Pair(1, -1))
    val newMatrix = createMatrix(matrix.height, matrix.width, 0)
    var sum: Int
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            sum = 0
            for ((first, second) in allAround) {
                val x = i + first
                val y = j + second
                if (y in 0 until matrix.height && x in 0 until matrix.width)
                    sum += matrix[y, x]
            }
            newMatrix[j, i] = sum
        }
    }
    return newMatrix
}

/**
 * Средняя
 *
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes {
    var trigger = true
    val rows = mutableListOf<Int>()
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            if (matrix[i, j] == 1) {
                trigger = false
                break
            }
        }
        if (trigger)
            rows.add(i)
        trigger = true
    }
    val columns = mutableListOf<Int>()
    for (j in 0 until matrix.width) {
        for (i in 0 until matrix.height) {
            if (matrix[i, j] == 1) {
                trigger = false
                break
            }
        }
        if (trigger)
            columns.add(j)
        trigger = true
    }
    return Holes(rows, columns)
}

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Средняя
 *
 * В целочисленной матрице matrix каждый элемент заменить суммой элементов подматрицы,
 * расположенной в левом верхнем углу матрицы matrix и ограниченной справа-снизу данным элементом.
 *
 * Пример для матрицы 3 х 3:
 *
 * 1  2  3      1  3  6
 * 4  5  6  =>  5 12 21
 * 7  8  9     12 27 45
 *
 * К примеру, центральный элемент 12 = 1 + 2 + 4 + 5, элемент в левом нижнем углу 12 = 1 + 4 + 7 и так далее.
 */
fun sumSubMatrix(matrix: Matrix<Int>): Matrix<Int> {
    val newMatrix = createMatrix(matrix.height, matrix.width, 0)
    var total: Int
    for (i in 0 until matrix.height) {
        total = 0
        for (j in 0 until matrix.width) {
            for (row in 0..i) total += matrix[row, j]
            newMatrix[i, j] = total
        }
    }
    return newMatrix
}

/**
 * Сложная
 *
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    for (row in 0..lock.height - key.height)
        for (column in 0..lock.width - key.width) {
            var trigger = false
            for (ySetOf in 0 until key.height) {
                for (xSetOf in 0 until key.width)
                    if (lock[row + ySetOf, column + xSetOf] == key[ySetOf, xSetOf]) {
                        trigger = true
                        break
                    }
                if (trigger) break
            }
            if (!trigger) return Triple(true, row, column)
        }
    return Triple(false, 0, 0)
}

/**
 * Простая
 *
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> {
    for (i in 0 until this.height)
        for (j in 0 until this.width)
            this[i, j] = -this[i, j]
    return this
}

/**
 * Средняя
 *
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> {
    require(this.width == other.height)
    val newMatrix = createMatrix(this.height, other.width, 0)
    var sum: Int
    for (row in 0 until this.height) {
        for (column in 0 until other.width) {
            sum = 0
            for (i in 0 until this.width) {
                sum += this[row, i] * other[i, column]
            }
            newMatrix[row, column] = sum
        }
    }
    return newMatrix
}

/**
 * Сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  1
 *  2 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой. Цель игры -- упорядочить фишки на игровом поле.
 *
 * В списке moves задана последовательность ходов, например [8, 6, 13, 11, 10, 3].
 * Ход задаётся номером фишки, которая передвигается на пустое место (то есть, меняется местами с нулём).
 * Фишка должна примыкать к пустому месту по горизонтали или вертикали, иначе ход не будет возможным.
 * Все номера должны быть в пределах от 1 до 15.
 * Определить финальную позицию после выполнения всех ходов и вернуть её.
 * Если какой-либо ход является невозможным или список содержит неверные номера,
 * бросить IllegalStateException.
 *
 * В данном случае должно получиться
 * 5  7  9  1
 * 2 12 14 15
 * 0  4 13  6
 * 3 10 11  8
 */

class PlayGrid(val grid: Matrix<Int>) {

    private fun findNum(num: Int): Pair<Int, Int> {
        for (row in 0 until grid.height) {
            for (column in 0 until grid.width) {
                if (grid[row, column] == num) {
                    return Pair(row, column)
                }
            }
        }
        return Pair(-1, -1)
    }

    class Tile(var row: Int, var col: Int)

    private val blank = Tile(findNum(0).first, findNum(0).second)

    private val allAround = listOf(Pair(0, 1), Pair(-1, 0), Pair(0, -1), Pair(1, 0))

    fun around(): List<Int> {
        val result = mutableListOf<Int>()
        for ((rowAdd, colAdd) in allAround) {
            val row = blank.row + rowAdd
            val col = blank.col + colAdd
            if (row in 0 until grid.height && col in 0 until grid.width) {
                result.add(grid[row, col])
            }
        }
        return result
    }

    fun numReplace(first: Int, second: Int) {
        val (firstRow, firstCol) = findNum(first)
        val (secondRow, secondCol) = findNum(second)
        replace(first, firstRow, firstCol, second, secondRow, secondCol)
    }

    private fun replace(first: Int, firstRow: Int, firstCol: Int, second: Int, secondRow: Int, secondCol: Int) {
        grid[firstRow, firstCol] = second
        grid[secondRow, secondCol] = first
    }

    fun isSolvable(): Boolean {
        val checked = mutableListOf<Int>()
        var count = 0
        for (row in 0 until grid.height) {
            for (col in 0 until grid.width) {
                val num = grid[row, col]
                if (num != 0) {
                    count += num - 1 - checked.count { it < num }
                    checked.add(num)
                }
            }
        }
        return grid.width % 2 != 0 || (count + blank.row) % 2 != 0
    }

    fun move(number: Int) {
        for ((rowAdd, colAdd) in allAround) {
            val row = blank.row + rowAdd
            val col = blank.col + colAdd
            if (row in 0 until grid.height && col in 0 until grid.width && grid[row, col] == number) {
                replace(0, blank.row, blank.col, number, row, col)
                blank.row = row
                blank.col = col
                return
            }
        }
        throw IllegalStateException()
    }
}

fun fifteenGameMoves(matrix: Matrix<Int>, moves: List<Int>): Matrix<Int> {
    val grid = PlayGrid(matrix)
    moves.forEach { grid.move(it) }
    return grid.grid
}

/**
 * Очень сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  2
 *  1 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой.
 *
 * Цель игры -- упорядочить фишки на игровом поле, приведя позицию к одному из следующих двух состояний:
 *
 *  1  2  3  4          1  2  3  4
 *  5  6  7  8   ИЛИ    5  6  7  8
 *  9 10 11 12          9 10 11 12
 * 13 14 15  0         13 15 14  0
 *
 * Можно математически доказать, что РОВНО ОДНО из этих двух состояний достижимо из любой исходной позиции.
 *
 * Вернуть решение -- список ходов, приводящих исходную позицию к одной из двух упорядоченных.
 * Каждый ход -- это перемена мест фишки с заданным номером с пустой клеткой (0),
 * при этом заданная фишка должна по горизонтали или по вертикали примыкать к пустой клетке (но НЕ по диагонали).
 * К примеру, ход 13 в исходной позиции меняет местами 13 и 0, а ход 11 в той же позиции невозможен.
 *
 * Одно из решений исходной позиции:
 *
 * [8, 6, 14, 12, 4, 11, 13, 14, 12, 4,
 * 7, 5, 1, 3, 11, 7, 3, 11, 7, 12, 6,
 * 15, 4, 9, 2, 4, 9, 3, 5, 2, 3, 9,
 * 15, 8, 14, 13, 12, 7, 11, 5, 7, 6,
 * 9, 15, 8, 14, 13, 9, 15, 7, 6, 12,
 * 9, 13, 14, 15, 12, 11, 10, 9, 13, 14,
 * 15, 12, 11, 10, 9, 13, 14, 15]
 *
 * Перед решением этой задачи НЕОБХОДИМО решить предыдущую
 */

fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {

    val playGrid = PlayGrid(matrix)

    val isSolvable = playGrid.isSolvable()
    if (!isSolvable) {
        playGrid.numReplace(14, 15)
    }

    fun hash(grid: Matrix<Int>): Pair<Long, Long> {
        var first: Long = 0
        var count = 0
        for (i in 0..1) {
            for (j in 0..3) {
                val num = grid[i, j]
                var sum = num
                for (k in 0 until count) {
                    sum *= 16
                }
                first += sum
                count++
            }
        }
        var second: Long = 0
        count = 0
        for (i in 2..3) {
            for (j in 0..3) {
                val num = grid[i, j]
                var sum = num
                for (k in 0 until count) {
                    sum *= 16
                }
                second += sum
                count++
            }
        }
        return Pair(first, second)
    }

    fun upDist(grid: Matrix<Int>): Int {
        var count = 0
        for (row in 0..3) {
            for (col in 0..3) {
                val num = grid[row, col]
                count += if (num != 0)
                    abs(row - (num - 1) / 4) + abs(col - (num - 1) % 4)
                else
                    (3 - row) + (3 - col)
            }
        }
        return count
    }

    if (upDist(playGrid.grid) == 0) return emptyList()

    class Node(val playGrid: PlayGrid, val dist: Int, val hash: Pair<Long, Long>)

    val firstHash = hash(playGrid.grid)
    val connections = mutableMapOf(firstHash to Pair(Pair<Long, Long>(0, 0), 0))
    val hashes = hashSetOf(firstHash)
    val nodes = PriorityQueue<Node>(compareBy { it.dist })
    nodes.add(Node(playGrid, upDist(playGrid.grid), firstHash))

    fun findWay(hash: Pair<Long, Long>): List<Int> {
        var nextHash = hash
        val moves = mutableListOf<Int>()
        while (nextHash != firstHash) {
            moves.add(connections[nextHash]!!.second)
            nextHash = connections[nextHash]!!.first
        }
        return if (isSolvable) moves.reversed()
        else moves.map {
            when (it) {
                14 -> 15
                15 -> 14
                else -> it
            }
        }.reversed()
    }

    while (true) {
        val node = nodes.poll()
        val possibleMoves = node.playGrid.around()
        for (move in possibleMoves) {
            val newPlayGrid = PlayGrid(copy(node.playGrid.grid))
            newPlayGrid.move(move)
            val newHash = hash(newPlayGrid.grid)
            if (!hashes.contains(newHash)) {
                val dist = upDist(newPlayGrid.grid)
                nodes.add(Node(newPlayGrid, dist, newHash))
                connections[newHash] = Pair(node.hash, move)
                hashes.add(newHash)
                if (dist == 0) return findWay(newHash)
            }
        }
    }
}
