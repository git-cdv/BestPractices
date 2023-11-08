package com.chkan.bestpractices.best_rv.core

sealed class ResultOf<T> {

    @Suppress("UNCHECKED_CAST")
    fun <R> map(mapper: (T) -> R): ResultOf<R> {
        if (this is SuccessResult) return SuccessResult(mapper(data))
        return this as ResultOf<R>
    }

}

class SuccessResult<T>(
    val data: T
) : ResultOf<T>()

class ErrorResult<T>(
    val error: Throwable
) : ResultOf<T>()

class PendingResult<T> : ResultOf<T>()

class EmptyResult<T> : ResultOf<T>()