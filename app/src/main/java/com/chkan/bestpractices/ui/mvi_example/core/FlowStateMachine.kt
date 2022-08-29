package com.chkan.bestpractices.ui.mvi_example.core

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * @author Dmytro Chkan on 24.08.2022.
 */
open class FlowStateMachine<G: Any, U: Any>(
    init: () -> CommonMachineState<G, U>
) : CommonStateMachine.Base<G, U>(init) {

    private val mediator = MutableSharedFlow<U>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        start()
    }

    /**
     * ExportedUI state
     */
    val uiState: SharedFlow<U> = mediator.asSharedFlow()

    final override fun setUiState(uiState: U) {
        mediator.tryEmit(uiState)
    }
}