package com.chkan.bestpractices.core

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * @author Dmytro Chkan on 20.05.2022.
 */

abstract class BaseFragment(@LayoutRes layout:Int) : Fragment(layout) {

    abstract fun initUI(savedInstanceState: Bundle?)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(savedInstanceState)
    }
}