package com.chkan.bestpractices.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentNotificationBinding
import kotlinx.coroutines.delay

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    companion object {
        private const val CHANNEL_ID = "com.chkan.bestpractic.channelid"
        private const val notificationId = 1111
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotificationChannel()

        binding.btnStart.setOnClickListener {
            lifecycleScope.launchWhenCreated {

                delay(5000)

                val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notif_icon)
                    .setContentTitle("Title")
                    .setContentText("textContent")
                    .setNumber(4)//set counter to icon badge
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

                with(NotificationManagerCompat.from(requireContext())) {
                    notify(notificationId, builder.build())
                }
            }

        }
    }

    private fun createNotificationChannel() {
        //It's safe to call this repeatedly because creating an existing notification channel performs no operation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Main channel"
            val descriptionText = "Description channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}