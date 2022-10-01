package com.chkan.bestpractices.simple_paging.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chkan.bestpractices.core.Dispatchers
import com.chkan.bestpractices.core.ResultType
import com.chkan.bestpractices.simple_paging.models.PassengersUIModel
import com.chkan.bestpractices.simple_paging.domain.usecases.GetPassengersUseCase
import com.chkan.bestpractices.simple_paging.models.mapToPassengersUI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SimplePagingViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val getDataUseCase : GetPassengersUseCase
): ViewModel(){

    private val listPassengersLiveData = MutableLiveData<List<PassengersUIModel>>()
    fun listPassengers() = listPassengersLiveData

    private val errorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    fun error() = errorLiveData

    private var currentPage = 0
    private var total = 1

    fun getPassengers(limit: Int, sizeList:Int) {
        dispatchers.launchBackground(viewModelScope) {
            if (total > sizeList) {
                val list = getDataUseCase.getPassengers(currentPage, limit)
                if (list.resultType == ResultType.SUCCESS) {
                    if (total == 1) total = list.data?.get(0)?.total ?: 1
                    listPassengersLiveData.postValue(list.data.mapToPassengersUI())
                    currentPage++
                }
            } else {
                errorLiveData.postValue(true)
            }
        }
    }

}