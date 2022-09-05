package com.chkan.bestpractices.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.extensions.activityNavController
import com.chkan.bestpractices.core.extensions.navigateSafely
import com.chkan.bestpractices.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSimplePaging.setOnClickListener {
            activityNavController().navigateSafely(R.id.action_menuFragment_to_SimplePagingFlowFragment)
        }
        binding.btnMvi.setOnClickListener {
            activityNavController().navigateSafely(R.id.action_menuFragment_to_mviListFragment)
        }
        binding.btnDropdownList.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_dropDownFragment)
        }

    }

}