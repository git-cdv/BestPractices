package com.chkan.bestpractices.tests

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author Dmytro Chkan on 19.09.2022.
 */
class CalculatorTest {

    lateinit var calculator: Calculator

    @Before
    fun setUp() {
        calculator = Calculator()
    }

    @After
    fun tearDown() {
        println("tearDown")
    }

    @Test
    fun add() {
        println("add")
        assertEquals(9, calculator.add(6,3))
    }

    @Test
    fun subtract() {
        println("subtract")
        assertEquals(3, calculator.subtract(6,3))
    }

    @Test
    fun multiply() {
        println("multiply")
        assertEquals(18, calculator.multiply(6,3))
    }

    @Test
    fun divide() {
        println("divide")
        assertEquals(2, calculator.divide(6,3))
    }
}