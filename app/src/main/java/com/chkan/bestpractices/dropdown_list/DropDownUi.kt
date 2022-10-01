package com.chkan.bestpractices.dropdown_list

import android.widget.TextView
import com.chkan.bestpractices.R

/**
 * @author Dmytro Chkan on 04.09.2022.
 */
abstract class DropDownUi(private val text: String, protected val id: Int) {

    abstract fun viewType(): Int

    abstract fun handleClick(clickCallback: ClickCallback)

    fun same(other: DropDownUi) = id == other.id

    fun sameContent(other: DropDownUi) =
        text + id == other.text + other.id && javaClass.name == other.javaClass.name

    fun show(textView: TextView) {
        textView.text = id.toString()
    }

    abstract class Expandable(text: String, id: Int) : DropDownUi(text, id) {
        override fun handleClick(clickCallback: ClickCallback) =
            clickCallback.changeCollapseState(id)
    }

    class Expanded(text: String, id: Int) : Expandable(text, id) {
        override fun viewType(): Int = R.layout.expanded_item_layout
    }

    class Collapsed(text: String, id: Int) : Expandable(text, id) {
        override fun viewType(): Int = R.layout.collapsed_item_layout
    }

    class NoNestedItems(text: String, id: Int) : DropDownUi(text, id) {
        override fun viewType(): Int = R.layout.no_nested_items_layout
        override fun handleClick(clickCallback: ClickCallback) = Unit
    }
}

interface ClickCallback {

    fun changeCollapseState(id: Int)
}