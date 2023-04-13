package com.chkan.bestpractices.simple_paging.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chkan.bestpractices.core.Dispatchers
import com.chkan.bestpractices.core.doIfFailure
import com.chkan.bestpractices.core.doIfSuccess
import com.chkan.bestpractices.simple_paging.models.PassengersUIModel
import com.chkan.bestpractices.simple_paging.domain.usecases.GetPassengersUseCase
import com.chkan.bestpractices.simple_paging.models.PassengersUIMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SimplePagingViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val getDataUseCase : GetPassengersUseCase,
    private val mapperToUI : PassengersUIMapper
): ViewModel(){

    private val listPassengersLiveData = MutableLiveData<List<PassengersUIModel>>()
    fun listPassengers() = listPassengersLiveData

    private val errorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    fun error() = errorLiveData

    private var currentPage = 0
    private var total = 1

    fun getPassengers(limit: Int, sizeList: Int) {
        dispatchers.launchBackground(viewModelScope) {
            if (total > sizeList) {
                val result = getDataUseCase.getPassengers(currentPage, limit)
                result.doIfSuccess { list ->
                    if (total == 1) total = list.firstOrNull()?.total ?: 1
                    listPassengersLiveData.postValue(mapperToUI.map(list))
                    currentPage++
                }
                result.doIfFailure { error, throwable ->
                    errorLiveData.postValue(true)
                }
            }
        }
    }

}