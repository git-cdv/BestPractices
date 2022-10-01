package com.chkan.bestpractices.dropdown_list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Dmytro Chkan on 04.09.2022.
 */
@Serializable
    class Wrapper(
        @SerialName("data")
        val data: List<DropDownRaw>
    )

    @Serializable
    class DropDownRaw(
        @SerialName("id")
        val id: Int,
        @SerialName("text")
        val text: String,
        @SerialName("parentId")
        val parentId: Int
    )
