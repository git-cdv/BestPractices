package com.chkan.bestpractices.auth.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentUserInfoBinding
import com.chkan.bestpractices.utils.auth.launchAndCollectIn
import com.kts.github.utils.resetNavGraph
import com.chkan.bestpractices.utils.auth.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : BaseFragment<FragmentUserInfoBinding> (FragmentUserInfoBinding::inflate) {

    private val viewModel: UserInfoViewModel by viewModels()

    private val logoutResponse = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            viewModel.webLogoutComplete()
        } else {
            // логаут отменен
            // делаем complete тк github не редиректит после логаута и пользователь закрывает CCT
            viewModel.webLogoutComplete()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.corruptAccessToken.setOnClickListener {
            viewModel.corruptAccessToken()
        }
        binding.getUserInfo.setOnClickListener {
            viewModel.loadUserInfo()
        }
        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.loadingFlow.launchAndCollectIn(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.getUserInfo.isEnabled = !isLoading
            binding.userInfo.isVisible = !isLoading
        }

        viewModel.userInfoFlow.launchAndCollectIn(viewLifecycleOwner) { userInfo ->
            binding.userInfo.text = userInfo?.login
        }

        viewModel.toastFlow.launchAndCollectIn(viewLifecycleOwner) {
            toast(it)
        }

        viewModel.logoutPageFlow.launchAndCollectIn(viewLifecycleOwner) {
            logoutResponse.launch(it)
        }

        viewModel.logoutCompletedFlow.launchAndCollectIn(viewLifecycleOwner) {
            findNavController().resetNavGraph(R.navigation.auth_graph)
        }
    }
}