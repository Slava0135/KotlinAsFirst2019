package lesson11.task1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import java.lang.ArithmeticException

internal class UnsignedBigIntegerTest {

    @Test
    @Tag("Normal")
    fun plus() {
        assertEquals(UnsignedBigInteger(4), UnsignedBigInteger(2) + UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger("9087654330"), UnsignedBigInteger("9087654329") + UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger("1000000000"), UnsignedBigInteger("999999999") + UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger("1" + "000000000" + "000000000"), UnsignedBigInteger("999999999" + "999999999") + UnsignedBigInteger(1))
    }

    @Test
    @Tag("Normal")
    fun minus() {
        assertEquals(UnsignedBigInteger(2), UnsignedBigInteger(4) - UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger("9087654329"), UnsignedBigInteger("9087654330") - UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger("999999999"), UnsignedBigInteger("1000000000") - UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger(0), UnsignedBigInteger(1) - UnsignedBigInteger(1))
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(2) - UnsignedBigInteger(4)
        }
    }

    @Test
    @Tag("Hard")
    fun times() {
        assertEquals(
            UnsignedBigInteger("10000000000000000"),
            UnsignedBigInteger(100000000) * UnsignedBigInteger(100000000)
        )
        assertEquals(
            UnsignedBigInteger("18446744073709551616"),
            UnsignedBigInteger("4294967296") * UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger("80779853376"),
            UnsignedBigInteger(123456) * UnsignedBigInteger(654321)
        )
        assertEquals(
            UnsignedBigInteger("4294967296"),
            UnsignedBigInteger("4294967296") * UnsignedBigInteger(1)
        )
        assertEquals(
            UnsignedBigInteger(0),
            UnsignedBigInteger(0) * UnsignedBigInteger("11111111111111111111111111111111111111")
        )
    }

    @Test
    @Tag("Impossible")
    fun div() {
        assertEquals(
            UnsignedBigInteger("4294967296"),
            UnsignedBigInteger("18446744073709551616") / UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger(0),
            UnsignedBigInteger(0) / UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger("184467440737095"),
            UnsignedBigInteger("18446744073709551616") / UnsignedBigInteger(100000)
        )
        assertEquals(
            UnsignedBigInteger(1),
            UnsignedBigInteger(999999999) / UnsignedBigInteger(999999998)
        )
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(1) / UnsignedBigInteger(0)
        }
        for (i in 0..10000) {
            UnsignedBigInteger("5475438975989473583459878754875484358433458435784538753487587345") / UnsignedBigInteger("4754757548484548573474858734")
        }
    }

    @Test
    @Tag("Impossible")
    fun rem() {
        assertEquals(UnsignedBigInteger(5), UnsignedBigInteger(19) % UnsignedBigInteger(7))
        assertEquals(
            UnsignedBigInteger(0),
            UnsignedBigInteger("18446744073709551616") % UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger(1),
            UnsignedBigInteger("18446744073709551617") % UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger("4294967295"),
            UnsignedBigInteger("18446744073709551615") % UnsignedBigInteger("4294967296")
        )
    }

    @Test
    @Tag("Normal")
    fun equals() {
        assertEquals(UnsignedBigInteger(123456789), UnsignedBigInteger("123456789"))
    }

    @Test
    @Tag("Normal")
    fun compareTo() {
        assertTrue(UnsignedBigInteger(123456789) < UnsignedBigInteger("9876543210"))
        assertTrue(UnsignedBigInteger("9876543210") > UnsignedBigInteger(123456789))
        assertTrue(UnsignedBigInteger("10000000000000") > UnsignedBigInteger(1000))
        assertTrue(UnsignedBigInteger("999999999") == UnsignedBigInteger(999999999))
    }

    @Test
    @Tag("Normal")
    fun toInt() {
        assertEquals(123456789, UnsignedBigInteger("123456789").toInt())
        assertEquals(Int.MAX_VALUE, UnsignedBigInteger(Int.MAX_VALUE).toInt())
        assertEquals(2000000000, UnsignedBigInteger(2000000000).toInt())
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger("1000000000000000000").toInt()
        }
    }
}