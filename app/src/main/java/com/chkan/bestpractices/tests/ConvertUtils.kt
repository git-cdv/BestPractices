package com.chkan.bestpractices.tests

/**
 * @author Dmytro Chkan on 19.09.2022.
 */
class ConvertUtils {

    fun stringToInteger(s: String): Int {
        var result = 0
        try {
            result = s.toInt()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return result
    }
}