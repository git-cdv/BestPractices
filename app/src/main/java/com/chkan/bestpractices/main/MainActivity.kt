package com.chkan.bestpractices.main

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.chkan.bestpractices.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    lateinit var fontSizeManager: FontSizeManager

    override fun attachBaseContext(newBase: Context) {
        fontSizeManager = FontSizeManager(newBase.prefs())
        val newConfig = Configuration(newBase.resources.configuration)
        newConfig.fontScale = fontSizeManager.fontSize.scale
        applyOverrideConfiguration(newConfig)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        navController = host.navController
    }

    fun updateFontSize(fontSize: FontSize) {
        fontSizeManager.fontSize = fontSize
        recreate()
    }
}

fun Context.prefs(): SharedPreferences = getSharedPreferences("main_prefs", Context.MODE_PRIVATE)

class FontSizeManager(private val prefs: SharedPreferences) {

    private val unsetFontSizeValue = -1f

    var fontSize: FontSize
        get() {
            val scale = prefs.getFloat("font_scale", unsetFontSizeValue)
            return if (scale == unsetFontSizeValue) {
                FontSize.DEFAULT
            } else {
                FontSize.values().first { fontSize -> fontSize.scale == scale }
            }
        }
        set(value) {
            prefs.edit()
                .putFloat("font_scale", value.scale)
                .apply()
        }

}

enum class FontSize(val scale: Float) {
    SMALL(0.7f),
    DEFAULT(1.0f),
    LARGE(1.3f)
}