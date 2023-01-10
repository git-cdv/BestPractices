package com.chkan.bestpractices.custom_view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentCustomViewBinding
import java.time.LocalDate

////https://github.com/silverxcoins/CustomView

class CustomViewFragment : BaseFragment<FragmentCustomViewBinding>(FragmentCustomViewBinding::inflate) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gantView = view.findViewById<GantView>(R.id.gant)
        val now = LocalDate.now()
        gantView.setTasks(
            listOf(
                Task(
                    name = "Task 1",
                    dateStart = now.minusMonths(1),
                    dateEnd = now
                ),
                Task(
                    name = "Task 2 long name",
                    dateStart = now.minusWeeks(2),
                    dateEnd = now.plusWeeks(1)
                ),
                Task(
                    name = "Task 3",
                    dateStart = now.minusMonths(2),
                    dateEnd = now.plusMonths(2)
                ),
                Task(
                    name = "Some Task 4",
                    dateStart = now.plusWeeks(2),
                    dateEnd = now.plusMonths(2).plusWeeks(1)
                ),
                Task(
                    name = "Task 5",
                    dateStart = now.minusMonths(2).minusWeeks(1),
                    dateEnd = now.plusWeeks(1)
                )
            )
        )
    }
}