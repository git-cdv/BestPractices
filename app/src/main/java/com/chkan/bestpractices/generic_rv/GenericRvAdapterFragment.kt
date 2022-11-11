package com.chkan.bestpractices.generic_rv

import android.os.Bundle
import android.view.View
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentGenericRvAdapterBinding

class GenericRvAdapterFragment : BaseFragment<FragmentGenericRvAdapterBinding>(
    FragmentGenericRvAdapterBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}