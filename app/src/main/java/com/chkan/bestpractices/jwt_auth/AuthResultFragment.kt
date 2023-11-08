package com.chkan.bestpractices.jwt_auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.extensions.onClick
import com.chkan.bestpractices.databinding.FragmentAuthResultBinding
import com.chkan.bestpractices.jwt_auth.data.utils.AuthResult
import com.chkan.bestpractices.utils.auth.launchAndCollectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthResultFragment : BaseFragment<FragmentAuthResultBinding>(FragmentAuthResultBinding::inflate) {

    private val viewModel: JwtAuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.onClick {
            findNavController().popBackStack()
        }
        binding.btnCheckToken.onClick{
            viewModel.checkToken()
        }
        viewModel.authResults.launchAndCollectIn(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    binding.textView.text = result.data.toString()
                }

                is AuthResult.Unauthorized -> {
                    binding.textView.text = ""
                    Toast.makeText(
                        context,
                        "You're not authorized",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is AuthResult.UnknownError -> {
                    binding.textView.text = ""
                    Toast.makeText(
                        context,
                        "An unknown error occurred",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}