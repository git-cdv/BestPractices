package com.chkan.bestpractices.best_rv.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chkan.bestpractices.R
import com.chkan.bestpractices.best_rv.core.BaseViewModel
import com.chkan.bestpractices.best_rv.core.EmptyResult
import com.chkan.bestpractices.best_rv.core.Event
import com.chkan.bestpractices.best_rv.core.PendingResult
import com.chkan.bestpractices.best_rv.core.ResultOf
import com.chkan.bestpractices.best_rv.core.SuccessResult
import com.chkan.bestpractices.best_rv.data.UsersService
import com.chkan.bestpractices.best_rv.data.models.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val usersService: UsersService
) : BaseViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast

    private val _actionGoBack = MutableLiveData<Event<Unit>>()
    val actionGoBack: LiveData<Event<Unit>> = _actionGoBack

    private val currentState: State get() = state.value!!

    init {
        _state.value = State(
            userDetailsResult = EmptyResult(),
            deletingInProgress = false
        )
    }

    fun loadUser(userId: Long) {
        // Do not start 2nd load user details request if we rotate
        // screen during loading details
        if (currentState.userDetailsResult !is EmptyResult) return

        _state.value = currentState.copy(userDetailsResult = PendingResult())

        usersService.getById(userId)
            .onSuccess {
                _state.value = currentState.copy(userDetailsResult = SuccessResult(it))
            }
            .onError {
                _actionShowToast.value = Event(R.string.cant_load_user_details)
                _actionGoBack.value = Event(Unit)
            }
            .autoCancel()
    }

    fun deleteUser() {
        val userDetailsResult = currentState.userDetailsResult
        if (userDetailsResult !is SuccessResult) return
        _state.value = currentState.copy(deletingInProgress = true)
        usersService.deleteUser(userDetailsResult.data.user)
            .onSuccess {
                _actionShowToast.value = Event(R.string.user_has_been_deleted)
                _actionGoBack.value = Event(Unit)
            }
            .onError {
                _state.value = currentState.copy(deletingInProgress = false)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }


    data class State(
        val userDetailsResult: ResultOf<UserDetails>,
        private val deletingInProgress: Boolean
    ) {

        val showContent: Boolean get() = userDetailsResult is SuccessResult
        val showProgress: Boolean get() = userDetailsResult is PendingResult || deletingInProgress
        val enableDeleteButton: Boolean get() = !deletingInProgress

    }
}