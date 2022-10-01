package com.chkan.bestpractices.mvi_example.model.state

import com.chkan.bestpractices.mvi_example.model.LceUiState

/**
 * Terminates activity
 */
class TerminatedState : LceLogicalState() {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(LceUiState.Terminated)
    }
}