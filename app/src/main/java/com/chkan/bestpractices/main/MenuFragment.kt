package com.chkan.bestpractices.main

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

        with(binding){
            btnSimplePaging.setOnClickListener {
                activityNavController().navigateSafely(R.id.action_menuFragment_to_SimplePagingFlowFragment)
            }
            btnMvi.setOnClickListener {
                activityNavController().navigateSafely(R.id.action_menuFragment_to_mviListFragment)
            }
            btnDropdownList.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_dropDownFragment)
            }
            btnAuth.setOnClickListener {
                findNavController().navigate(R.id.auth_graph)
            }
            btnLocation.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_locationFragment)
            }
        }
    }

}