package com.chkan.bestpractices.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.maxElementForXLarge
import com.chkan.bestpractices.core.observe
import com.chkan.bestpractices.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by activityViewModels()

    private var isLoading = true

    private val adapter = ListPassengersAdapter (PassListListener { item ->
        Log.d("CHKAN", "item : $item")
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvPassengers.adapter = adapter

        initViewModel()
        initPaging()
    }

    private fun initPaging() = with(binding.rvPassengers) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lms = recyclerView.layoutManager as LinearLayoutManager
                var lastVisiblePosition = 0

                lastVisiblePosition = lms.findLastCompletelyVisibleItemPosition()

                val countItem = lms.itemCount

                val isLastPosition = countItem.minus(
                    requireContext().maxElementForXLarge(5, 6)
                ) < lastVisiblePosition && lastVisiblePosition < lms.itemCount

                if (!isLoading && isLastPosition) {
                    isLoading = true
                    viewModel.getPassengers(requireContext().maxElementForXLarge(28, 50))
                }
            }
        })
    }

    private fun initViewModel() = with(viewModel) {
        getPassengers(requireContext().maxElementForXLarge(28, 50))

        observe(listPassengers()) {
            isLoading = false
            adapter.setList(it ?: listOf())
        }
    }

}