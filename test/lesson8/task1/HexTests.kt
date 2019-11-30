package lesson8.task1

import lesson8.task1.Direction.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class HexTests {

    @Test
    @Tag("Normal")
    fun hexPointDistance() {
        assertEquals(5, HexPoint(6, 1).distance(HexPoint(1, 4)))
        assertEquals(8, HexPoint(2, 1).distance(HexPoint(7, 4)))
    }

    @Test
    @Tag("Normal")
    fun hexagonDistance() {
        assertEquals(2, Hexagon(HexPoint(1, 3), 1).distance(Hexagon(HexPoint(6, 2), 2)))
    }

    @Test
    @Tag("Trivial")
    fun hexagonContains() {
        assertTrue(Hexagon(HexPoint(3, 3), 1).contains(HexPoint(2, 3)))
        assertFalse(Hexagon(HexPoint(3, 3), 1).contains(HexPoint(4, 4)))
    }

    @Test
    @Tag("Easy")
    fun hexSegmentValid() {
        assertTrue(HexSegment(HexPoint(1, 3), HexPoint(5, 3)).isValid())
        assertTrue(HexSegment(HexPoint(3, 1), HexPoint(3, 6)).isValid())
        assertTrue(HexSegment(HexPoint(1, 5), HexPoint(4, 2)).isValid())
        assertFalse(HexSegment(HexPoint(3, 1), HexPoint(6, 2)).isValid())
    }

    @Test
    @Tag("Normal")
    fun hexSegmentDirection() {
        assertEquals(RIGHT, HexSegment(HexPoint(1, 3), HexPoint(5, 3)).direction())
        assertEquals(UP_RIGHT, HexSegment(HexPoint(3, 1), HexPoint(3, 6)).direction())
        assertEquals(DOWN_RIGHT, HexSegment(HexPoint(1, 5), HexPoint(4, 2)).direction())
        assertEquals(LEFT, HexSegment(HexPoint(5, 3), HexPoint(1, 3)).direction())
        assertEquals(DOWN_LEFT, HexSegment(HexPoint(3, 6), HexPoint(3, 1)).direction())
        assertEquals(UP_LEFT, HexSegment(HexPoint(4, 2), HexPoint(1, 5)).direction())
        assertEquals(INCORRECT, HexSegment(HexPoint(3, 1), HexPoint(6, 2)).direction())
    }

    @Test
    @Tag("Easy")
    fun oppositeDirection() {
        assertEquals(LEFT, RIGHT.opposite())
        assertEquals(DOWN_LEFT, UP_RIGHT.opposite())
        assertEquals(UP_LEFT, DOWN_RIGHT.opposite())
        assertEquals(RIGHT, LEFT.opposite())
        assertEquals(DOWN_RIGHT, UP_LEFT.opposite())
        assertEquals(UP_RIGHT, DOWN_LEFT.opposite())
        assertEquals(INCORRECT, INCORRECT.opposite())
    }

    @Test
    @Tag("Normal")
    fun nextDirection() {
        assertEquals(UP_RIGHT, RIGHT.next())
        assertEquals(UP_LEFT, UP_RIGHT.next())
        assertEquals(RIGHT, DOWN_RIGHT.next())
        assertEquals(DOWN_LEFT, LEFT.next())
        assertEquals(LEFT, UP_LEFT.next())
        assertEquals(DOWN_RIGHT, DOWN_LEFT.next())
        assertThrows(IllegalArgumentException::class.java) {
            INCORRECT.next()
        }
    }

    @Test
    @Tag("Easy")
    fun isParallelDirection() {
        assertTrue(RIGHT.isParallel(RIGHT))
        assertTrue(RIGHT.isParallel(LEFT))
        assertFalse(RIGHT.isParallel(UP_LEFT))
        assertFalse(RIGHT.isParallel(INCORRECT))
        assertTrue(UP_RIGHT.isParallel(UP_RIGHT))
        assertTrue(UP_RIGHT.isParallel(DOWN_LEFT))
        assertFalse(UP_RIGHT.isParallel(UP_LEFT))
        assertFalse(INCORRECT.isParallel(INCORRECT))
        assertFalse(INCORRECT.isParallel(UP_LEFT))
    }

    @Test
    @Tag("Normal")
    fun hexPointMove() {
        assertEquals(HexPoint(3, 3), HexPoint(0, 3).move(RIGHT, 3))
        assertEquals(HexPoint(3, 5), HexPoint(5, 3).move(UP_LEFT, 2))
        assertEquals(HexPoint(5, 0), HexPoint(5, 4).move(DOWN_LEFT, 4))
        assertEquals(HexPoint(1, 1), HexPoint(1, 1).move(DOWN_RIGHT, 0))
        assertEquals(HexPoint(4, 2), HexPoint(2, 2).move(LEFT, -2))
        assertThrows(IllegalArgumentException::class.java) {
            HexPoint(0, 0).move(INCORRECT, 0)
        }
    }

    @Test
    @Tag("Hard")
    fun pathBetweenHexes() {
        assertEquals(
            listOf(
                HexPoint(y = 2, x = 2),
                HexPoint(y = 2, x = 3),
                HexPoint(y = 3, x = 3),
                HexPoint(y = 4, x = 3),
                HexPoint(y = 5, x = 3)
            ), pathBetweenHexes(HexPoint(y = 2, x = 2), HexPoint(y = 5, x = 3))
        )
    }

    @Test
    @Tag("Impossible")
    fun hexagonByThreePoints() {
        assertNotNull(
            hexagonByThreePoints(HexPoint(-558, -999), HexPoint(-558, 199), HexPoint(-441, -999))
        )
        assertEquals(
            Hexagon(HexPoint(4, 3), 3),
            hexagonByThreePoints(HexPoint(1, 3), HexPoint(4, 6), HexPoint(7, 0))
        )
        assertNotNull(
            hexagonByThreePoints(HexPoint(719, -1000), HexPoint(-999, -1000), HexPoint(-955, -557))
        )
        assertNotNull(
            hexagonByThreePoints(HexPoint(-760, 148), HexPoint(136, -1000), HexPoint(-676, -999))
        )
        assertEquals(
            Hexagon(HexPoint(4, 3), 3),
            hexagonByThreePoints(HexPoint(1, 4), HexPoint(1, 5), HexPoint(6, 4))
        )
        assertEquals(
            Hexagon(HexPoint(4, 2), 2),
            hexagonByThreePoints(HexPoint(3, 1), HexPoint(2, 3), HexPoint(4, 4))
        )
        assertNull(
            hexagonByThreePoints(HexPoint(3, 1), HexPoint(2, 3), HexPoint(5, 4))
        )
        assertEquals(
            3,
            hexagonByThreePoints(HexPoint(2, 3), HexPoint(3, 3), HexPoint(5, 3))?.radius
        )
        assertEquals(
            Hexagon(HexPoint(-618, -617), 382),
            hexagonByThreePoints(HexPoint(-338, -999), HexPoint(-1000, -266), HexPoint(-237, -999))
        )
    }

    @Test
    @Tag("Impossible")
    fun minContainingHexagon() {
        val points2 = parse("""{
    "x": 813,
    "y": -558
  },
  {
    "x": -557,
    "y": -557
  },
  {
    "x": -999,
    "y": -557
  },
  {
    "x": -419,
    "y": 252
  },
  {
    "x": -999,
    "y": -557
  },
  {
    "x": -851,
    "y": -999
  },
  {
    "x": -1000,
    "y": 101
  },
  {
    "x": -558,
    "y": 331
  },
  {
    "x": -1000,
    "y": -1000
  },
  {
    "x": -562,
    "y": 346
  },
  {
    "x": -720,
    "y": 702
  },
  {
    "x": -1000,
    "y": -558
  },
  {
    "x": -999,
    "y": -646
  },
  {
    "x": -999,
    "y": -558
  },
  {
    "x": 140,
    "y": 531
  },
  {
    "x": -999,
    "y": -767
  },
  {
    "x": 361,
    "y": -1000
  },
  {
    "x": -999,
    "y": -190
  },
  {
    "x": -999,
    "y": -999
  },
  {
    "x": -876,
    "y": -118
  },
  {
    "x": 944,
    "y": -557
  },
  {
    "x": -145,
    "y": 393
  },
  {
    "x": -557,
    "y": 853
  },
  {
    "x": 116,
    "y": -999
  },
  {
    "x": 278,
    "y": -1000
  },
  {
    "x": -999,
    "y": 677
  },
  {
    "x": -558,
    "y": -6
  },
  {
    "x": -138,
    "y": -355
  },
  {
    "x": -558,
    "y": -871
  },
  {
    "x": -353,
    "y": 218
  },
  {
    "x": -523,
    "y": -468
  },
  {
    "x": -999,
    "y": -333
  },
  {
    "x": -855,
    "y": -558
  },
  {
    "x": -999,
    "y": 129
  },
  {
    "x": 209,
    "y": -558
  },
  {
    "x": -558,
    "y": -557
  },
  {
    "x": -618,
    "y": -1000
  },
  {
    "x": -735,
    "y": -649
  },
  {
    "x": 406,
    "y": -999
  },
  {
    "x": 590,
    "y": -999
  },
  {
    "x": -999,
    "y": 980
  },
  {
    "x": -557,
    "y": -301
  },
  {
    "x": -558,
    "y": -557
  },
  {
    "x": -883,
    "y": -562
  },
  {
    "x": -666,
    "y": 666
  },
  {
    "x": -1000,
    "y": -999
  },
  {
    "x": -933,
    "y": -265
  },
  {
    "x": -557,
    "y": 958
  },
  {
    "x": -999,
    "y": 199
  },
  {
    "x": -999,
    "y": -1000
  },
  {
    "x": -557,
    "y": -1000
  },
  {
    "x": -557,
    "y": -557
  },
  {
    "x": -999,
    "y": 704
  },
  {
    "x": -1000,
    "y": 300
  },
  {
    "x": -999,
    "y": -217
  },
  {
    "x": -557,
    "y": -1000
  },
  {
    "x": -999,
    "y": -999
  },
  {
    "x": 721,
    "y": -172
  },
  {
    "x": -459,
    "y": 66
  },
  {
    "x": -1000,
    "y": 678
  },
  {
    "x": -405,
    "y": 148
  },
  {
    "x": -557,
    "y": 364
  },
  {
    "x": 864,
    "y": -999
  },
  {
    "x": -558,
    "y": -558
  },
  {
    "x": -558,
    "y": -558
  },
  {
    "x": -999,
    "y": -999
  },
  {
    "x": -557,
    "y": -309
  },
  {
    "x": 725,
    "y": -286
  },
  {
    "x": 829,
    "y": -1000
  },
  {
    "x": -557,
    "y": -815
  },
  {
    "x": -557,
    "y": 736
  },
  {
    "x": -558,
    "y": 853
  },
  {
    "x": -557,
    "y": -557""")
        val result2 = minContainingHexagon(*points2)
        assertTrue(1337 >= result2.radius)
        val points = arrayOf(HexPoint(3, 1), HexPoint(3, 2), HexPoint(5, 4), HexPoint(8, 1))
        val result = minContainingHexagon(*points)
        assertEquals(3, result.radius)
        assertTrue(points.all { result.contains(it) })
        assertNotNull(minContainingHexagon(HexPoint(3, 1), HexPoint(2, 3), HexPoint(5, 4)))
        val anotherPoints = arrayOf(
            HexPoint(478, -557),
            HexPoint(-558, 825),
            HexPoint(-999, -557),
            HexPoint(165, 688),
            HexPoint(-225, -1000),
            HexPoint(-764, 219),
            HexPoint(-957, -558),
            HexPoint(159, -97),
            HexPoint(880, -557)
        )
        val anotherResult = minContainingHexagon(*anotherPoints)
        assertTrue(1244 > anotherResult.radius)
        assertTrue(anotherPoints.all { anotherResult.contains(it) })
    }
}