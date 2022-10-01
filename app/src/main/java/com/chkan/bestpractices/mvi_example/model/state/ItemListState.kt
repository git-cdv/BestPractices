package com.chkan.bestpractices.mvi_example.model.state

import android.util.Log
import com.chkan.bestpractices.mvi_example.model.LceGesture
import com.chkan.bestpractices.mvi_example.model.LceUiState
import com.chkan.bestpractices.mvi_example.data.ItemId
import com.chkan.bestpractices.mvi_example.data.ItemModel

/**
 * Item list
 * Processes [LceGesture.ItemClicked] and transfers to loading state
 */
class ItemListState : LceLogicalState() {

    private val items = listOf(
        ItemId.LOADS_CONTENT to "Item that loads",
        ItemId.FAILS_WITH_ERROR to "Item that fails to load"
    )

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Log.d("CHKAN","Displaying item list")
        setUiState(LceUiState.ItemList(items.map { ItemModel(it.first, it.second) }))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LceGesture) = when(gesture) {
        is LceGesture.ItemClicked -> onItemClicked(gesture.id)
        is LceGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onItemClicked(id: ItemId) {
        Log.d("CHKAN","Item clicked: transferring to loading...")
        setMachineState(LoadingState(id))
    }

    private fun onBack() {
        Log.d("CHKAN","Back: Terminating flow...")
        setMachineState(TerminatedState())
    }
}