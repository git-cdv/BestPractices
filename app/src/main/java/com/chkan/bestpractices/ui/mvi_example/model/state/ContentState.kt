
package com.chkan.bestpractices.ui.mvi_example.model.state

import com.chkan.bestpractices.ui.mvi_example.model.LceGesture
import com.chkan.bestpractices.ui.mvi_example.model.LceUiState


/**
 * Displays item details
 * Processes [LceGesture.Back] to transfer back to list
 * @param contents Loaded contents
 */
class ContentState(private val contents: String) : LceLogicalState() {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(LceUiState.Item(contents))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LceGesture) = when (gesture) {
        LceGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        setMachineState(ItemListState())
    }
}