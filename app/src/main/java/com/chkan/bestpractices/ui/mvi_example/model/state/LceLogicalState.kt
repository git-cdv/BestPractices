
package com.chkan.bestpractices.ui.mvi_example.model.state

import com.chkan.bestpractices.ui.mvi_example.core.CommonMachineState
import com.chkan.bestpractices.ui.mvi_example.model.LceGesture
import com.chkan.bestpractices.ui.mvi_example.model.LceUiState

/**
 * Basic flow state
 */
abstract class LceLogicalState : CommonMachineState<LceGesture, LceUiState>() {
    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LceGesture) {

    }
}