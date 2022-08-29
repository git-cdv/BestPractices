package com.chkan.bestpractices.ui.mvi_example.model

import com.chkan.bestpractices.ui.mvi_example.data.ItemId

/**
 * @author Dmytro Chkan on 24.08.2022.
 */
sealed class LceGesture {
    /**
     * Item to load clicked
     * @property id Item ID to load
     */
    data class ItemClicked(val id: ItemId) : LceGesture()

    /**
     * Retry operation clicked
     */
    object Retry : LceGesture()

    /**
     * Backwards navigation gesture
     */
    object Back : LceGesture()

    /**
     * Terminates activity
     */
    object Exit : LceGesture()

    /**
     * Unknown action
     */
    object Unknown : LceGesture()
}