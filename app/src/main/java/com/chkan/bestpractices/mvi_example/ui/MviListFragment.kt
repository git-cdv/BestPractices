package com.chkan.bestpractices.mvi_example.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentMviListBinding
import com.chkan.bestpractices.mvi_example.model.LceGesture
import com.chkan.bestpractices.mvi_example.model.LceUiState
import com.chkan.bestpractices.mvi_example.model.LceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MviListFragment : BaseFragment<FragmentMviListBinding>(FragmentMviListBinding::inflate) {

    private val viewModel: LceViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when(it){
                    is LceUiState.ItemList -> {
                        binding.btn1.apply {
                            isVisible = true
                            text = it.items[0].name
                            setOnClickListener { view ->
                                viewModel.process(LceGesture.ItemClicked(it.items[0].id))
                            }
                        }
                        binding.btn2.apply {
                            isVisible = true
                            text = it.items[1].name
                            setOnClickListener { view ->
                                viewModel.process(LceGesture.ItemClicked(it.items[1].id))
                            }
                        }
                        binding.bar.isVisible = false
                    }
                    is LceUiState.Loading -> {
                        binding.btn1.isVisible = false
                        binding.btn2.isVisible = false
                        binding.bar.isVisible = true
                    }
                    is LceUiState.Item -> {
                        binding.btn1.isVisible = false
                        binding.btn2.isVisible = false
                        binding.textView.text = it.contents
                        binding.bar.isVisible = false
                    }
                    is LceUiState.Error -> {
                        binding.btn1.isVisible = false
                        binding.btn2.isVisible = false
                        binding.textView.text = "ERROR"
                        binding.bar.isVisible = false
                    }
                }

            }
        }

    }

}