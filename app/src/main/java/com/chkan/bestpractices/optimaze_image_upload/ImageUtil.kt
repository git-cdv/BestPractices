package com.chkan.bestpractices.optimaze_image_upload

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri

interface ImageUtil {
    suspend fun getImageFileSize(ctx: Context, imageUri: Uri): Long
    suspend fun decodeImageMetaData(ctx: Context, imageUri: Uri): BitmapFactory.Options?
}