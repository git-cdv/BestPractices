package com.chkan.bestpractices.change_text_size

import android.os.Bundle
import android.view.View
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentFontSizeBinding
import com.chkan.bestpractices.main.FontSize
import com.chkan.bestpractices.main.MainActivity


class FontSizeFragment : BaseFragment<FragmentFontSizeBinding>(FragmentFontSizeBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.small.setOnClickListener {
            (activity as MainActivity).updateFontSize(FontSize.SMALL)
        }
        binding.middle.setOnClickListener {
            (activity as MainActivity).updateFontSize(FontSize.DEFAULT)
        }
        binding.large.setOnClickListener {
            (activity as MainActivity).updateFontSize(FontSize.LARGE)
        }

    }
}