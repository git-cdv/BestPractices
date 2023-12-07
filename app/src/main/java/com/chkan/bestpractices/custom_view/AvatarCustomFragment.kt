package com.chkan.bestpractices.custom_view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.extensions.onClick
import com.chkan.bestpractices.databinding.FragmentAvatarCustomBinding

class AvatarCustomFragment : BaseFragment<FragmentAvatarCustomBinding>(FragmentAvatarCustomBinding::inflate) {

    var text = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSetter.onClick {
            binding.labelAvatar.visibility = View.VISIBLE
            text += "0"
            binding.labelAvatar.setTextAndColor(text, Color.RED)
        }
    }

}