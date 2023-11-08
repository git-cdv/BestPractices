package com.chkan.bestpractices.jwt_auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.extensions.onClick
import com.chkan.bestpractices.databinding.FragmentJwtAuthBinding
import com.chkan.bestpractices.jwt_auth.data.utils.AuthResult
import com.chkan.bestpractices.utils.auth.launchAndCollectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JwtAuthFragment : BaseFragment<FragmentJwtAuthBinding>(FragmentJwtAuthBinding::inflate) {

    private val viewModel: JwtAuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUp.onClick {
            val userName = binding.etSignUpName.text.toString()
            val pw = binding.etSignUpPw.text.toString()
            viewModel.signUp(userName, pw)
        }
        binding.btnSignIn.onClick {
            val userName = binding.etSignInName.text.toString()
            val pw = binding.etSignInPw.text.toString()
            viewModel.signIn(userName, pw)
        }
        viewModel.authResults.launchAndCollectIn(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    findNavController().navigate(R.id.action_jwtAuthFragment_to_authResultFragment)
                }

                is AuthResult.Unauthorized -> {
                    Toast.makeText(
                        context,
                        "You're not authorized",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is AuthResult.UnknownError -> {
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