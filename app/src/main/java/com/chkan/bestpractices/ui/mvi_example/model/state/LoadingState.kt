
package com.chkan.bestpractices.ui.mvi_example.model.state

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.chkan.bestpractices.ui.mvi_example.core.CoroutineState
import com.chkan.bestpractices.ui.mvi_example.model.LceGesture
import com.chkan.bestpractices.ui.mvi_example.model.LceUiState
import com.chkan.bestpractices.ui.mvi_example.data.ItemId
import kotlinx.coroutines.*
import java.io.IOException

/**
 * Loads item contents
 * Processes [LceGesture.Back] to transfer back to list
 * Transfers to error or to detail view
 * @param id Item ID to load
 * @param defaultDispatcher Dispatcher to run load on (used for testing)
 */
class LoadingState(
    @get:VisibleForTesting val id: ItemId,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : CoroutineState<LceGesture, LceUiState>() {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(LceUiState.Loading)
        load()
    }

    private fun load() {
        stateScope.launch(defaultDispatcher) {
            delay(1000L)
            withContext(Dispatchers.Main) {
                when (id) {
                    ItemId.LOADS_CONTENT -> toContent()
                    ItemId.FAILS_WITH_ERROR -> toError()
                }
            }
        }
    }

    private fun toContent() {
        Log.d("CHKAN","Data loaded: transferring to content...")
        setMachineState(ContentState("Some item data..."))
    }

    private fun toError() {
        Log.d("CHKAN","Data load error: transferring to error...")
        setMachineState(ErrorState(id, IOException("Failed to load item")))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LceGesture) = when(gesture) {
        LceGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        Log.d("CHKAN","Back: returning to item view")
        setMachineState(ItemListState())
    }
}