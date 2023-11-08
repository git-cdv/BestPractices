package com.chkan.bestpractices.best_rv.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chkan.bestpractices.R
import com.chkan.bestpractices.best_rv.core.BaseViewModel
import com.chkan.bestpractices.best_rv.core.EmptyResult
import com.chkan.bestpractices.best_rv.core.ErrorResult
import com.chkan.bestpractices.best_rv.core.Event
import com.chkan.bestpractices.best_rv.core.PendingResult
import com.chkan.bestpractices.best_rv.core.ResultOf
import com.chkan.bestpractices.best_rv.core.SuccessResult
import com.chkan.bestpractices.best_rv.data.UsersListener
import com.chkan.bestpractices.best_rv.data.UsersService
import com.chkan.bestpractices.best_rv.data.models.User
import com.chkan.bestpractices.best_rv.data.models.UserListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val usersService: UsersService
) : BaseViewModel(), UserActionListener {

    private val _users = MutableLiveData<ResultOf<List<UserListItem>>>()
    val users: LiveData<ResultOf<List<UserListItem>>> = _users

    private val _actionShowDetails = MutableLiveData<Event<User>>()
    val actionShowDetails: LiveData<Event<User>> = _actionShowDetails

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast

    private val userIdsInProgress = mutableSetOf<Long>()
    private var usersResult: ResultOf<List<User>> = EmptyResult()
        set(value) {
            field = value
            notifyUpdates()
        }

    private val listener: UsersListener = {
        usersResult = if (it.isEmpty()) {
            EmptyResult()
        } else {
            SuccessResult(it)
        }
    }

    init {
        usersService.addListener(listener)
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)
    }

    fun loadUsers() {
        usersResult = PendingResult()
        usersService.loadUsers()
            .onError {
                usersResult = ErrorResult(it)
            }
            .autoCancel()
    }

    override fun onUserMove(user: User, moveBy: Int) {
        if (isInProgress(user)) return
        addProgressTo(user)
        usersService.moveUser(user, moveBy)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_move_user)
            }
            .autoCancel()
    }

    override fun onUserDelete(user: User) {
        if (isInProgress(user)) return
        addProgressTo(user)
        usersService.deleteUser(user)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }

    override fun onUserFire(user: User) {
        if (isInProgress(user)) return
        addProgressTo(user)
        usersService.fireUser(user)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }

    override fun onUserDetails(user: User) {
        _actionShowDetails.value = Event(user)
    }

    private fun addProgressTo(user: User) {
        userIdsInProgress.add(user.id)
        notifyUpdates()
    }

    private fun removeProgressFrom(user: User) {
        userIdsInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User): Boolean {
        return userIdsInProgress.contains(user.id)
    }

    private fun notifyUpdates() {
        _users.postValue(usersResult.map { users ->
            users.map { user -> UserListItem(user, isInProgress(user)) }
        })
    }

}