package com.chkan.bestpractices.alarm_manager

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import com.chkan.bestpractices.R
import java.io.File


class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: return
        println("Alarm triggered: $message")
        val mp = MediaPlayer()
        val soundUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + File.pathSeparator + File.separator + File.separator
                    + context.packageName
                    + File.separator
                    + R.raw.bell
        )
        mp.setDataSource(context, soundUri)
        mp.setAudioStreamType(AudioManager.STREAM_ALARM)
        mp.prepareAsync()
        mp.setOnPreparedListener { player ->
            player.start()
        }
        mp.setOnCompletionListener {
            it.stop()
        }
    }
}