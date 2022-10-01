package com.chkan.bestpractices.simple_paging.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.maxElementForXLarge
import com.chkan.bestpractices.core.observe
import com.chkan.bestpractices.databinding.FragmentSimplePagingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimplePagingFragment : BaseFragment<FragmentSimplePagingBinding>(FragmentSimplePagingBinding::inflate) {

    //scope depends on life graph
    //implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")
    private val viewModel: SimplePagingViewModel by hiltNavGraphViewModels(R.id.simple_paging_graph)

    private var isLoading = true
    private var sizeList = 0

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
                    setLoading(true)
                    viewModel.getPassengers(requireContext().maxElementForXLarge(28, 50),sizeList)
                }
            }
        })
    }

    private fun initViewModel() = with(viewModel) {
        getPassengers(requireContext().maxElementForXLarge(28, 50),sizeList)

        observe(listPassengers()) {
            setLoading(false)
            it?.let {
                adapter.setList(it,sizeList)
                sizeList += it.size
            }
        }
        observe(error()){
            if(it == true) setLoading(false)
        }
    }

    fun setLoading(state: Boolean) {
        binding.pbPaging.isVisible = state
        isLoading = state
    }
}