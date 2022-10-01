package com.chkan.bestpractices.mvi_example.model.state

import com.chkan.bestpractices.mvi_example.model.LceGesture
import com.chkan.bestpractices.mvi_example.model.LceUiState
import com.chkan.bestpractices.mvi_example.data.ItemId

/**
 * Item load error state
 * Processes [LceGesture.Back] to transfer back to list
 * Processes [LceGesture.Retry] to retry load
 * Processes [LceGesture.Exit] to exit flow
 * @param error Load error
 */
class ErrorState(private val failed: ItemId, private val error: Throwable) : LceLogicalState() {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(LceUiState.Error(error))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LceGesture) = when (gesture) {
        LceGesture.Back -> onBack()
        LceGesture.Retry -> onRetry()
        LceGesture.Exit -> onExit()
        else -> super.doProcess(gesture)
    }

    private fun onRetry() {
        setMachineState(LoadingState(failed))
    }

    private fun onBack() {
        setMachineState(ItemListState())
    }

    private fun onExit() {
        setMachineState(TerminatedState())
    }
}