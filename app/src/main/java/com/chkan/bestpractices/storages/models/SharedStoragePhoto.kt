package com.chkan.bestpractices.storages.models

import android.net.Uri

/**
 * @author Dmytro Chkan on 11.12.2022.
 */
data class SharedStoragePhoto(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val contentUri: Uri
)