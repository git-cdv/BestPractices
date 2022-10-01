package com.chkan.bestpractices.dropdown_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentDropDownBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DropDownFragment : BaseFragment<FragmentDropDownBinding>(FragmentDropDownBinding::inflate) {

    private val viewModel: DropDownViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RVAdapter(viewModel)
        binding.rvDropdown.adapter = adapter
        viewModel.observe(this) {
            adapter.update(it)
        }

    }

}