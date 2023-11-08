package com.chkan.bestpractices.jwt_auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chkan.bestpractices.jwt_auth.data.utils.AuthResult
import com.chkan.bestpractices.jwt_auth.data.JwtAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JwtAuthViewModel @Inject constructor(
    private val repository: JwtAuthRepository
): ViewModel() {

    private val resultChannel = Channel<AuthResult<String>>()
    val authResults = resultChannel.receiveAsFlow()

    init {
        //authenticate()
    }

    fun signUp(username: String, password: String) {
        viewModelScope.launch {
            val result = repository.signUp(
                username = username,
                password = password
            )
            resultChannel.send(result)
        }
    }

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            val result = repository.signIn(
                username = username,
                password = password
            )
            resultChannel.send(result)
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            val result = repository.authenticate()
            resultChannel.send(result)
        }
    }

    fun checkToken() {
        viewModelScope.launch {
            val result = repository.getInfo()
            resultChannel.send(result)
        }
    }

}