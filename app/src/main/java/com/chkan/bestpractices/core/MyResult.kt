package com.chkan.bestpractices.core

import java.lang.Exception

data class MyResult<out T>(
    var resultType: ResultType,
    val data: T? = null,
    val error: Exception? = null
) {

    companion object {
        fun <T> success(data: T?): MyResult<T> {
            return MyResult(ResultType.SUCCESS, data)
        }

        fun <T> error(error: Exception? = null): MyResult<T> {
            return MyResult(ResultType.ERROR, error = error)
        }

        fun <T> empty(): MyResult<T> {
            return MyResult(ResultType.EMPTY)
        }
    }
}

enum class ResultType {
    ERROR,
    SUCCESS,
    EMPTY
}