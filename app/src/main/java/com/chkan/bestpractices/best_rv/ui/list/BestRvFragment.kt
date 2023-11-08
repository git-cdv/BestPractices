package com.chkan.bestpractices.best_rv.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.chkan.bestpractices.R
import com.chkan.bestpractices.best_rv.core.EmptyResult
import com.chkan.bestpractices.best_rv.core.ErrorResult
import com.chkan.bestpractices.best_rv.core.PendingResult
import com.chkan.bestpractices.best_rv.core.SuccessResult
import com.chkan.bestpractices.best_rv.ui.details.ARG_USER_ID
import com.chkan.bestpractices.databinding.FragmentBestRvBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BestRvFragment : Fragment() {

    /*
    https://github.com/romychab/android-tutorials/tree/recycler-view-diffutils/recycler-view-1

    * ВАЖНО: Чтобы нормально работал DiffUtil, нужно перед сетингом обновленного списка или элемента создавать НОВЫЙ список
    * или элемент чтобы DiffUtil УВИДЕЛ ИЗМЕНЕНИЯ и правильно отобразил (проблема с мутабельными списками)
    * UsersService -> #49
    * val updatedUser = users[index].copy(company = "")
        users = ArrayList(users)
        users[index] = updatedUser
        notifyChanges()

    * ФИШКИ:
    * 1. Кликабельные Views: item_user.xml -> #8, #54  android:background="?android:attr/selectableItemBackground"
    * 2. Сокращение лямбд : UsersService -> typealias UsersListener = (users: List<User>) -> Unit
    * 3. Замена элементов в коллекции: UsersService -> Collections.swap(users, oldIndex, newIndex)
    * 4. Убирает анимацию, которая бликает при нажатии: this -> #66
    * 5. Из-за того что в data class переопределен equals(), то UsersDiffCallback -> areContentsTheSame -> return oldUser == newUser -
    * проверяет равенство всех полей
    * */

    private lateinit var adapter: UsersBestAdapter
    private lateinit var binding: FragmentBestRvBinding
    private val viewModel: UsersListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBestRvBinding.inflate(inflater, container, false)
        adapter = UsersBestAdapter(viewModel)

        viewModel.users.observe(viewLifecycleOwner) {
            hideAll()
            when (it) {
                is SuccessResult -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.users = it.data
                }

                is ErrorResult -> {
                    binding.tryAgainContainer.visibility = View.VISIBLE
                }

                is PendingResult -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is EmptyResult -> {
                    binding.noUsersTextView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.actionShowDetails.observe(viewLifecycleOwner) {
            it.getValue()?.let { user -> findNavController().navigate(R.id.action_bestRvFragment_to_userDetailsFragment, bundleOf(ARG_USER_ID to user.id)) }
        }
        viewModel.actionShowToast.observe(viewLifecycleOwner) {
            it.getValue()?.let { messageRes -> Toast.makeText(requireActivity(),getText(messageRes), Toast.LENGTH_LONG).show() }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = adapter
        //убирает анимацию которая бликает при нажатии
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun hideAll() {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tryAgainContainer.visibility = View.GONE
        binding.noUsersTextView.visibility = View.GONE
    }
}