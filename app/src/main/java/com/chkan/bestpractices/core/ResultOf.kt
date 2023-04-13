package com.chkan.bestpractices.core


sealed class ResultOf<out T> {
    data class Success<out R>(val value: R): ResultOf<R>()
    data class Error (val message: String?, val throwable: Throwable?): ResultOf<Nothing>()
    object Loading : ResultOf<Nothing>()
}

inline fun <reified T> ResultOf<T>.doIfFailure(callback: (error: String?, throwable: Throwable?) -> Unit) {
    if (this is ResultOf.Error) {
        callback(message, throwable)
    }
}

inline fun <reified T> ResultOf<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is ResultOf.Success) {
        callback(value)
    }
}
