package com.chkan.bestpractices.coroutines.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class CoroutineScenariosViewModel @Inject constructor(): ViewModel(){

    // BLOCK VALIDATION
    private val _name = MutableStateFlow("")
    private val _age = MutableStateFlow("")

    val dataIsValid: LiveData<Boolean> = combine(_name, _age) { name, age ->
        isNameValid(name) && isAgeValid(age)
    }.asLiveData()

    private fun isAgeValid(age: String): Boolean {
        return age.toIntOrNull() != null
    }

    private fun isNameValid(name: String): Boolean {
        return name.length > 4
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setAge(age: String) {
        _age.value = age
    }
    //////

    // BLOCK PERIODIC WORK

    var startData = 0

    fun fetchDataWithPeriod(): Flow<Int> = flow {
        while(true) {
            val data = startData++
            emit(data)
            delay(5_000)
        }
    }
}
