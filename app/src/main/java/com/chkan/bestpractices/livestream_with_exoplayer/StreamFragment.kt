package com.chkan.bestpractices.livestream_with_exoplayer

import android.annotation.SuppressLint
import android.os.Build
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.ExoPlayer
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentStreamBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StreamFragment : BaseFragment<FragmentStreamBinding>(FragmentStreamBinding::inflate) {
    /*
        В XML:
        app:use_controller="true" - показывать или нет кнопки управления
        app:resize_mode="zoom" - как располагается на экране
     */


    private var player: ExoPlayer? = null
    //запуск сразу после загрузки (без нажатия на Play)
    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L
    private fun initializePlayer() {
        player = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer

                // Update the track selection parameters to only pick standard definition tracks
                exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                    .buildUpon()
                    .setMaxVideoSizeSd()
                    .build()

                //https://tvrain.tv/live/
                val mediaItem = MediaItem.Builder()
                    .setUri("https://wl.tvrain.tv/transcode/ngrp:ses_all/chunklist_b1128000.m3u8")
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .build()

                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            mediaItemIndex = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Build.VERSION.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, binding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}