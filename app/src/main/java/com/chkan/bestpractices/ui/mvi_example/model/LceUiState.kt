package com.chkan.bestpractices.ui.mvi_example.model

import com.chkan.bestpractices.ui.mvi_example.data.ItemModel
import javax.annotation.concurrent.Immutable

/**
 * @author Dmytro Chkan on 24.08.2022.
 */
sealed class LceUiState {
    /**
     * Item list view
     */
    @Immutable
    data class ItemList(val items: List<ItemModel>) : LceUiState()

    /**
     * Loading spinner
     */
    object Loading : LceUiState()

    /**
     * Item detail view
     */
    data class Item(val contents: String) : LceUiState()

    /**
     * Error view
     */
    data class Error(val error: Throwable) : LceUiState()

    /**
     * Terminator
     */
    object Terminated : LceUiState()
}