package com.chkan.bestpractices.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chkan.bestpractices.core.Dispatchers
import com.chkan.bestpractices.core.ResultType
import com.chkan.bestpractices.ui.models.PassengersUIModel
import com.chkan.bestpractices.domain.usecases.GetPassengersUseCase
import com.chkan.bestpractices.ui.models.mapToPassengersUI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val getDataUseCase : GetPassengersUseCase
): ViewModel(){

    private val listPassengersLiveData = MutableLiveData<List<PassengersUIModel>>()
    fun listPassengers() = listPassengersLiveData

    private val errorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    fun error() = errorLiveData

    private var currentPage = 0
    private var total = 1

    fun getPassengers(size: Int) {
        dispatchers.launchBackground(viewModelScope) {
            val currentList = listPassengersLiveData.value?.toMutableList() ?: mutableListOf()
            if (total > currentList.size) {
                val list = getDataUseCase.getPassengers(currentPage, size)
                if (list.resultType == ResultType.SUCCESS) {
                    currentList.addAll(list.data.mapToPassengersUI())
                    if (total == 1) total = list.data?.get(0)?.total ?: 1
                    listPassengersLiveData.postValue(currentList)
                    currentPage++
                }
            } else {
                errorLiveData.postValue(true)
            }
        }
    }

}