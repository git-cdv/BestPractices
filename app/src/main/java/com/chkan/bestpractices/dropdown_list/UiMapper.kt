package com.chkan.bestpractices.dropdown_list

import javax.inject.Inject

/**
 * @author Dmytro Chkan on 04.09.2022.
 */
interface UiMapper : Mapper<List<DropDownUi>, DropDownList> {

    class Base @Inject constructor() : UiMapper {
        override fun map(source: DropDownList): List<DropDownUi> {
            val list = mutableListOf<DropDownUi>()

            source.addUi(object : AddUiCallback {
                override fun add(item: DropDownUi) { list.add(item) }
            })
            return list
        }
    }
}

interface AddUiCallback {
    fun add(item: DropDownUi)
}