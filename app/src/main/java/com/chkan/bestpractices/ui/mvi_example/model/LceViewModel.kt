package com.chkan.bestpractices.ui.mvi_example.model

import androidx.lifecycle.ViewModel
import com.chkan.bestpractices.ui.mvi_example.core.CommonMachineState
import com.chkan.bestpractices.ui.mvi_example.core.FlowStateMachine
import com.chkan.bestpractices.ui.mvi_example.model.state.ItemListState
import kotlinx.coroutines.flow.SharedFlow

/**
 * @author Dmytro Chkan on 24.08.2022.
 */
/**
 * Wraps state-machine with view-model
 */
class LceViewModel : ViewModel() {
    /**
     * Creates initial state for state-machine
     */
    private fun initStateMachine(): CommonMachineState<LceGesture, LceUiState> = ItemListState()

    /**
     * State-machine instance
     */
    private val stateMachine = FlowStateMachine(::initStateMachine)

    /**
     * UI State
     */
    val state: SharedFlow<LceUiState> = stateMachine.uiState

    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: LceGesture) {
        stateMachine.process(gesture)
    }

    override fun onCleared() {
        stateMachine.clear()
    }
}