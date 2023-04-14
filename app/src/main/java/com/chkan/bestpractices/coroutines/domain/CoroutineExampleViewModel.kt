package com.chkan.bestpractices.coroutines.domain

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chkan.bestpractices.core.doIfFailure
import com.chkan.bestpractices.core.doIfSuccess
import com.chkan.bestpractices.coroutines.domain.usecases.AddUserUseCase
import com.chkan.bestpractices.coroutines.domain.usecases.DeleteAllUsersUseCase
import com.chkan.bestpractices.coroutines.domain.usecases.FetchOrGetUsersUseCase
import com.chkan.bestpractices.coroutines.domain.usecases.GetUsersUseCase
import com.chkan.bestpractices.simple_paging.models.PassengersUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoroutineExampleViewModel @Inject constructor(
    getUsersFlowUseCase: GetUsersUseCase,
    private val addUsersUseCase : AddUserUseCase,
    private val deleteUsersUseCase : DeleteAllUsersUseCase,
    private val fetchOrGetUsersUseCase : FetchOrGetUsersUseCase
): ViewModel(){

    var users = getUsersFlowUseCase.run().asLiveData()
    var getDataJob: Job? = null

    private val listUsersLiveData = MutableLiveData<List<User>>()
    fun listUsers() = listUsersLiveData

    private val _searchQuery = MutableStateFlow("")

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("CHKAN", "handled $exception" )
    }

    val searchResultLiveData = _searchQuery.asStateFlow()
        .debounce(500)
        .filter { it.length > 3 }
        .mapLatest { query ->
            //mapLatest - отменяет предыдущее "вычисление", если пришло новое
            filteredList(query)
        }.asLiveData()

    private fun filteredList(query: String) : List<User> {
        return listUsersLiveData.value?.filter { it.name == query} ?: mutableListOf()
    }

    fun search (query: String) {
        _searchQuery.value = query
    }

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

    fun fetchUsersInParallel() {
        viewModelScope.launch (handler) {
            val result1 = async { fetchOrGetUsersUseCase.runInParallel(0) }
            val result2 = async { fetchOrGetUsersUseCase.runInParallel(1) }
            listUsersLiveData.value = result1.await() + result2.await()
            /*
            Ошибка придет в await() и если нужно продолжить код в корутине дальше, то нужно обернуть в try-catch

            Важное замечание. Не убирайте handler из launch, даже если await обернуты в try-catch.
            Потому что async в любом случае передаст ошибку в launch, а ему понадобится хэндлер, чтобы обработать ошибку и не крэшнуть.

            Больше вариантов обработки ошибок:
            https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/622-urok-27-korutiny-praktika-parallelnye-vyzovy.html
            */
        }
    }

}