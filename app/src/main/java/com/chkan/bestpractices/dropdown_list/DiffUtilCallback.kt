package com.chkan.bestpractices.dropdown_list

import androidx.recyclerview.widget.DiffUtil

/**
 * @author Dmytro Chkan on 04.09.2022.
 */
class DiffUtilCallback(
    private val oldList: List<DropDownUi>,
    private val newList: List<DropDownUi>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].sameContent(newList[newItemPosition])
}