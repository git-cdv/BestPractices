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

    var currentPage = 0

    fun getPassengers(size:Int) {
        dispatchers.launchBackground(viewModelScope){
            val list = getDataUseCase.getPassengers(currentPage,size)
            if(list.resultType == ResultType.SUCCESS){
                val total = list.data?.get(0)?.total
                val currentList = listPassengersLiveData.value?.toMutableList() ?: mutableListOf()
                if (total != null) {
                    if(total > currentList.size){
                        val result = list.data.mapToPassengersUI()
                        currentList.addAll(result)
                        listPassengersLiveData.postValue(currentList)
                        Log.d("CHKAN", "currentList size - ${currentList.size}")
                        currentPage++
                    }
                }
            } else{
                errorLiveData.postValue(true)
            }
        }
    }

}