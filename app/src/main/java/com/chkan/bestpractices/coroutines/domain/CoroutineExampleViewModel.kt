package com.chkan.bestpractices.coroutines.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chkan.bestpractices.core.doIfFailure
import com.chkan.bestpractices.core.doIfSuccess
import com.chkan.bestpractices.coroutines.domain.usecases.AddUserUseCase
import com.chkan.bestpractices.coroutines.domain.usecases.DeleteAllUsersUseCase
import com.chkan.bestpractices.coroutines.domain.usecases.FetchOrGetUsersUseCase
import com.chkan.bestpractices.coroutines.domain.usecases.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoroutineExampleViewModel @Inject constructor(
    private val getUsersFlowUseCase: GetUsersUseCase,
    private val addUsersUseCase : AddUserUseCase,
    private val deleteUsersUseCase : DeleteAllUsersUseCase,
    private val fetchOrGetUsersUseCase : FetchOrGetUsersUseCase
): ViewModel(){

    val users = getUsersFlowUseCase.run().asLiveData()
    var getDataJob: Job? = null

    fun onAddClick(name:String) {
        viewModelScope.launch {
            val result = addUsersUseCase.run(User(name))
            with(result){
                doIfSuccess {
                    Log.d("CHKAN", "addUsers - Success" )
                }
                doIfFailure { error, throwable ->
                    Log.d("CHKAN", "addUsers with error - $error" )
                }
            }
        }
    }

    fun onClearClick() {
        viewModelScope.launch {
            deleteUsersUseCase.run()
        }
    }

    fun fetchUsers() {
        if (getDataJob?.isActive == true) return //Защита от повторных вызовов

        getDataJob = viewModelScope.launch {
            val result = fetchOrGetUsersUseCase.run()
            with(result){
                doIfSuccess {
                    Log.d("CHKAN", "fetchUsers - Success" )
                }
                doIfFailure { error, throwable ->
                    Log.d("CHKAN", "fetchUsers with error - $error" )
                }
            }
        }
    }

}