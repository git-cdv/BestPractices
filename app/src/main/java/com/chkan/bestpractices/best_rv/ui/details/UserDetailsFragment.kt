package com.chkan.bestpractices.best_rv.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.chkan.bestpractices.R
import com.chkan.bestpractices.best_rv.core.SuccessResult
import com.chkan.bestpractices.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

const val ARG_USER_ID = "ARG_USER_ID"

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailsBinding
    private val viewModel: UserDetailsViewModel by viewModels()

    private val userId by lazy {
        arguments?.getLong(ARG_USER_ID) ?: 1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUser(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(layoutInflater, container, false)

        viewModel.actionShowToast.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let { messageRes -> Toast.makeText(requireActivity(),getText(messageRes), Toast.LENGTH_LONG).show() }
        })
        viewModel.actionGoBack.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let { findNavController().popBackStack() }
        })

        viewModel.state.observe(viewLifecycleOwner, Observer {
            binding.contentContainer.visibility = if (it.showContent) {
                val userDetails = (it.userDetailsResult as SuccessResult).data
                binding.userNameTextView.text = userDetails.user.name
                if (userDetails.user.photo.isNotBlank()) {
                    Glide.with(this)
                        .load(userDetails.user.photo)
                        .circleCrop()
                        .into(binding.photoImageView)
                } else {
                    Glide.with(this)
                        .load(R.drawable.ic_user_avatar)
                        .into(binding.photoImageView)
                }
                binding.userDetailsTextView.text = userDetails.details

                View.VISIBLE
            } else {
                View.GONE
            }

            binding.progressBar.visibility = if (it.showProgress) View.VISIBLE else View.GONE
            binding.deleteButton.isEnabled = it.enableDeleteButton
        })

        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
        }

        return binding.root
    }
}