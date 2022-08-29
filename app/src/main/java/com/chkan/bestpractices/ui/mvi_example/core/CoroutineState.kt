package com.chkan.bestpractices.ui.mvi_example.core

import kotlinx.coroutines.*

/**
 * @author Dmytro Chkan on 24.08.2022.
 */
abstract class CoroutineState<G: Any, U: Any>: CommonMachineState<G, U>() {

    protected val stateScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun doClear() {
        stateScope.cancel()
    }
}