package com.chkan.bestpractices.tests.junit

interface ErrorHandler<R> {
    /**
     * Called when some consumer has been failed with the
     * specified [exception] while processing the [resource].
     */
    fun onError(exception: Exception, resource: R)
}