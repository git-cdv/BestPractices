package com.chkan.bestpractices.lottie_animation

import android.os.Bundle
import android.view.View
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentLottieBinding

class LottieFragment : BaseFragment<FragmentLottieBinding>(FragmentLottieBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener {
            binding.animationView.playAnimation()
        }

        binding.btnStop.setOnClickListener {
            binding.animationView.frame = 1
        }

    }

}