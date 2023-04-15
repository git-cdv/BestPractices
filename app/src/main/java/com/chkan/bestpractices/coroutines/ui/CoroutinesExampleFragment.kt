package com.chkan.bestpractices.coroutines.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.extensions.onClick
import com.chkan.bestpractices.core.observe
import com.chkan.bestpractices.coroutines.domain.CoroutineExampleViewModel
import com.chkan.bestpractices.databinding.FragmentCoroutinesExampleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoroutinesExampleFragment : BaseFragment<FragmentCoroutinesExampleBinding>(
    FragmentCoroutinesExampleBinding::inflate) {

    private val viewModel: CoroutineExampleViewModel by viewModels()
    private val adapter = UsersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initViewModel()
        initClicks()
        initSearch()
    }

    private fun initSearch() {
        binding.etSearch.doAfterTextChanged { viewModel.search(it.toString()) }
    }

    private fun initClicks() {
        with(binding) {
            addUser.onClick {
                viewModel.onAddClick(binding.etUsers.text.toString())
            }
            clearUsers.onClick {
                viewModel.onClearClick()
            }
            fetchUsers.onClick {
                viewModel.fetchUsers()
            }
            fetchParallel.onClick {
                viewModel.fetchUsersInParallel()
            }
        }

    }

    private fun initRecyclerView() {
        binding.rvUsers.adapter = adapter
    }

    private fun initViewModel() = with(viewModel) {
        observe(users){
            adapter.setList(it ?: mutableListOf())
        }
        observe(listUsers()){
            adapter.setList(it ?: mutableListOf())
        }
        observe(searchResultLiveData){
            adapter.setList(it ?: mutableListOf())
        }
    }
}