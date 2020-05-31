package com.roby.pretty

import com.roby.pretty.ext.halfOf
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun addition_isCorrect2() {
        assertEquals(4, 8.halfOf())
        assertEquals(6F, 12F.halfOf())
        assertEquals(4.4, 8.8.halfOf())
    }
}
