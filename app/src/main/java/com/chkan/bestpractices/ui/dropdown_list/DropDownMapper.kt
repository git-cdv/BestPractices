package com.chkan.bestpractices.ui.dropdown_list

import javax.inject.Inject

/**
 * @author Dmytro Chkan on 04.09.2022.
 */
interface DropDownMapper : Mapper<DropDownList, List<DropDownRaw>> {

    class Base @Inject constructor() : DropDownMapper {
        override fun map(source: List<DropDownRaw>): DropDownList {
            val noParentIds = mutableListOf<Int>()
            val result = DropDownList()

            //filtering main elements and add in noParentIds list
            source.filter { it.parentId == 0 }.forEach { item ->
                noParentIds.add(item.id)
                result.add(DropDown(item.id, item.text))
            }

            source.filter { !noParentIds.contains(it.id) }//убираем "сироты"
                .forEach { dropDownRaw -> //проходим по детям
                    for (it in result) { //берем каждую сироту и проверяем на родительство текущий элемент
                        if (it.add(dropDownRaw)) //проверяем входит ли он в верхний эелемент или во внутрение элементы
                            break
                    }
                }
            return result
        }
    }
}