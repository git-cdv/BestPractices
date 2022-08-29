package com.chkan.bestpractices.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.extensions.activityNavController
import com.chkan.bestpractices.core.extensions.navigateSafely
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialButton>(R.id.btn_simple_paging).setOnClickListener {
            activityNavController().navigateSafely(R.id.action_global_pagingFlowFragment)
        }
    }

}