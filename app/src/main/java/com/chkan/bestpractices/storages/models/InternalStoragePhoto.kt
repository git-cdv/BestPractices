package com.chkan.bestpractices.storages.models

import android.graphics.Bitmap

/**
 * @author Dmytro Chkan on 11.12.2022.
 */
data class InternalStoragePhoto(
    val name: String,
    val bmp: Bitmap
)