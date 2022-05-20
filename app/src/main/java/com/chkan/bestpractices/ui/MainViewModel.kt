package com.chkan.bestpractices.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chkan.bestpractices.core.Dispatchers
import com.chkan.bestpractices.data.MyResult
import com.chkan.bestpractices.data.ResultType
import com.chkan.bestpractices.data.models.ResponsePassengers
import com.chkan.bestpractices.domain.GetDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val getDataUseCase : GetDataUseCase
): ViewModel(){

    init {
        getPassengers()
    }

    private val listPassengersLiveData = MutableLiveData<MyResult<ResponsePassengers>>()
    fun listPassengers() = listPassengersLiveData

    private val errorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    fun error() = errorLiveData

    private fun getPassengers() {
        dispatchers.launchBackground(viewModelScope){
            val list = getDataUseCase.getPassengers(0,10)
            if(list.resultType == ResultType.SUCCESS){
                listPassengersLiveData.postValue(list)
            } else{
                errorLiveData.postValue(true)
            }
        }
    }

}