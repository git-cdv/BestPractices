package com.chkan.bestpractices.ui.dropdown_list

/**
 * @author Dmytro Chkan on 04.09.2022.
 */
class DropDownList : ArrayList<DropDown>(), ClickCallback {

    fun init(vararg elements: DropDown) {
        addAll(elements)
    }

    fun addUi(addCallback: AddUiCallback) {
        for (item in this) {
            if (item.inner.isEmpty())
                addCallback.add(DropDownUi.NoNestedItems(item.text, item.id))
            else
                if (item.isCollapsed)
                    addCallback.add(DropDownUi.Collapsed(item.text, item.id))
                else {
                    addCallback.add(DropDownUi.Expanded(item.text, item.id))
                    item.inner.addUi(addCallback)
                }
        }
    }

    override fun changeCollapseState(id: Int) {
        for (item in this) {
            if (item.changeCollapseState(id))
                break
        }
    }
}