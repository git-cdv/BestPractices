package com.chkan.bestpractices.ui

import androidx.lifecycle.LiveData
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

    private val _listPassengersLiveData = MutableLiveData<MyResult<ResponsePassengers>>()
    val listPassengersLiveData: LiveData<MyResult<ResponsePassengers>>
        get() = _listPassengersLiveData

    private val _isErrorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isErrorLiveData: LiveData<Boolean>
        get() = _isErrorLiveData

    private fun getPassengers() {
        dispatchers.launchBackground(viewModelScope){
            val list = getDataUseCase.getPassengers(0,10)
            if(list.resultType == ResultType.SUCCESS){
                _listPassengersLiveData.postValue(list)
            } else{
                _isErrorLiveData.postValue(true)
            }
        }
    }

}