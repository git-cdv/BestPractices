package com.chkan.bestpractices.ui

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun initUI(savedInstanceState: Bundle?) {
        initMainViewModel()
    }

    private fun initMainViewModel() = with(viewModel){
        observe(listPassengers()){
            val text = it?.data?.list?.size.toString()
            view?.findViewById<TextView>(R.id.text_fragment)?.text = text
        }
    }
}