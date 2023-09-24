package com.chkan.bestpractices.best_rv

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chkan.bestpractices.best_rv.data.User
import com.chkan.bestpractices.best_rv.data.UsersListener
import com.chkan.bestpractices.best_rv.data.UsersService
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentBestRvBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BestRvFragment : BaseFragment<FragmentBestRvBinding>(FragmentBestRvBinding::inflate) {

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

    @Inject
    lateinit var usersService: UsersService

    private lateinit var adapter: UsersBestAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)

        adapter = UsersBestAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int, userPosition: Int) {
                usersService.moveUser(user, moveBy)
                // решает проблему подымания первого элемента
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (userPosition == firstVisibleItemPosition || (userPosition == firstVisibleItemPosition + 1 && moveBy < 0)) {
                    val v = binding.recyclerView.getChildAt(0)
                    val offset = if (v == null) 0 else v.top - binding.recyclerView.paddingTop
                    layoutManager.scrollToPositionWithOffset(firstVisibleItemPosition, offset)
                }
            }

            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(context, "User: ${user.name}", Toast.LENGTH_SHORT).show()
            }

            override fun onUserFire(user: User) {
                usersService.fireUser(user)
            }
        })
        initRecyclerView()
        usersService.addListener(usersListener)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        //убирает анимацию которая бликает при нажатии
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    private val usersListener: UsersListener = {
        adapter.users = it
    }
}