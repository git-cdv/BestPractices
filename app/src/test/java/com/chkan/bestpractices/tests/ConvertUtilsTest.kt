package com.chkan.bestpractices.tests

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

/**
 * @author Dmytro Chkan on 19.09.2022.
 */
class ConvertUtilsTest {

    lateinit var converter: ConvertUtils

    @Before
    fun setUp() {
        converter = ConvertUtils()
    }

    @Test
    fun stringToInteger() {
        assertEquals(2, converter.stringToInteger("2"))
        assertEquals(-2, converter.stringToInteger("-2"))
        assertEquals(0, converter.stringToInteger(""))
        assertEquals(0, converter.stringToInteger("a"))
    }
}