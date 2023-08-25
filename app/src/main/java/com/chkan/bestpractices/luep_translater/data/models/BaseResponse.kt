package com.chkan.bestpractices.luep_translater.data.models

import java.io.Serializable

@kotlinx.serialization.Serializable
open class BaseResponse<out T> : Serializable {
    val success: Boolean = false
    val data: T? = null
    val error: String? = null

    override fun toString(): String {
        return "BaseResponse(success=$success, data=$data, error=$error)"
    }
}