package com.chkan.bestpractices.coroutines.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.extensions.onClick
import com.chkan.bestpractices.core.observe
import com.chkan.bestpractices.databinding.FragmentCoroutinesScenariosBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoroutinesScenariosFragment : BaseFragment<FragmentCoroutinesScenariosBinding>(
        FragmentCoroutinesScenariosBinding::inflate) {

    // https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/625-urok-30-korutiny-praktika-razlichnye-scenarii.html
    private val viewModel: CoroutineScenariosViewModel by viewModels()

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            initViewModel()
            initValidationDataScenario()
            initPeriodicWorkScenario()
        }

    private fun initPeriodicWorkScenario() {
        binding.btnPeriodic.onClick {
            lifecycleScope.launch {
                viewModel.fetchDataWithPeriod()
                    .catch {
                    //catch exc
                    }
                    .collect{
                    binding.tvPeriodicResult.text = it.toString()
                }
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        observe(dataIsValid){
            binding.btnSubmit.isEnabled = it ?: false
        }
    }

    private fun initValidationDataScenario() {
        binding.etName.doAfterTextChanged { viewModel.setName(it.toString()) }
        binding.etAge.doAfterTextChanged { viewModel.setAge(it.toString()) }
    }
}