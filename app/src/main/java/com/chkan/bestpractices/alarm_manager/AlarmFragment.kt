package com.chkan.bestpractices.alarm_manager

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(FragmentAlarmBinding::inflate) {

    @Inject
    lateinit var scheduler: AndroidAlarmScheduler
    var alarmItem: AlarmItem? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSchedule.setOnClickListener {
            val time = binding.etTime.text.toString()
            val message = binding.etMessage.text.toString()
            alarmItem = AlarmItem(
                time = LocalDateTime.now()
                    .plusSeconds(time.toLong()),
                message = message
            )
            alarmItem?.let(scheduler::schedule)
        }

        binding.btnCancel.setOnClickListener {
            alarmItem?.let(scheduler::cancel)
        }

    }

}

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)