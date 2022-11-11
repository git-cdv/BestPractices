package com.chkan.bestpractices.bipmap_with_stroke

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentBipmapBinding

class BipmapFragment : BaseFragment<FragmentBipmapBinding>(FragmentBipmapBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext())
            .asBitmap()
            .load("https://mommys.com.ua/image/cache/catalog/Mini/sumka_dlya_mam_print_800-1000x1000.jpg")
            .circleCrop()
            .error(R.drawable.ic_launcher_background)
            .into(onAvatarLoaded)
    }

    private val onAvatarLoaded = object : CustomTarget<Bitmap?>() {
        override fun onResourceReady(
            resource: Bitmap,
            transition: Transition<in Bitmap?>?
        ) {
            try {
                //удобно когда нужно нарисовать иконку для bottomNavigation
                //bottomNavigation.menu.getItem(BottomMenuPosition.MENU.ordinal).icon = result
                //val bmp = getRoundedBitmap(resource, Color.GREEN)
                //ДРУГИЕ РЕШЕНИЯ: https://stackoverflow.com/questions/34037939/how-to-make-border-for-circle-cropped-image-in-glide
                val bmp = getRoundedBitmap(resource, Color.GREEN)
                binding.ivAvatar.setImageDrawable(bmp)
            } catch (ex: Exception) {

            }
        }

        override fun onLoadCleared(placeholder: Drawable?) = Unit
    }
    private fun getRoundedBitmap(bmp: Bitmap, colorStroke: Int): RoundedBitmapDrawable {
        val rounded = RoundedBitmapDrawableFactory.create(
            resources,
            bmp.addBorder(200,Color.RED,colorStroke))
        rounded.isCircular = true
        return rounded
    }
}

fun Bitmap.addBorder(bgSize: Int, @ColorInt bgColor: Int, @ColorInt strokeColor: Int): Bitmap? {
    val stroke = 40
    val borderSize = bgSize+stroke
    val w = this.width + borderSize
    val h = this.height + borderSize
    val bmpWithBorder =
        Bitmap.createBitmap(w, h, this.config)
    val canvas = Canvas(bmpWithBorder)
    canvas.drawColor(bgColor)
    val cx = w / 2
    val cy = h / 2

    val radius = if (w > h) {
        (this.height / 2)+stroke
    } else {
        (this.width / 2)+stroke
    }
    canvas.drawCircle(cx.toFloat(),cy.toFloat(),radius.toFloat(),
        Paint().apply { color = strokeColor })
    canvas.drawBitmap(this, (borderSize/2).toFloat(), (borderSize/2).toFloat(), null)
    return bmpWithBorder
}