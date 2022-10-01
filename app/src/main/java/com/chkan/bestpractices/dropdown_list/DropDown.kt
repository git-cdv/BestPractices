package com.chkan.bestpractices.dropdown_list

/**
 * @author Dmytro Chkan on 04.09.2022.
 */
data class DropDown(
    val id: Int,
    val text: String,
    val inner: DropDownList = DropDownList(),
    var isCollapsed: Boolean = true
) {

    fun add(item: DropDownRaw): Boolean =
        if (item.parentId == id) {
            inner.add(DropDown(item.id, item.text))
            true
        } else {
            var found = false
            for (element in inner) {
                if (element.add(item)) {
                    found = true
                    break
                }
            }
            found
        }

    fun changeCollapseState(id: Int): Boolean =
        if (id == this.id) {
            isCollapsed = !isCollapsed
            true
        } else {
            var done = false
            for (item in inner) {
                if (item.changeCollapseState(id)) {
                    done = true
                    break
                }
            }
            done
        }
}