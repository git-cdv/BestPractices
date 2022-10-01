package com.chkan.bestpractices.dropdown_list

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Dmytro Chkan on 04.09.2022.
 */

@HiltViewModel
class DropDownViewModel @Inject constructor(
    repository: Repository,
    private val mapper: UiMapper
): ViewModel(), ClickCallback {

    private val liveData = MutableLiveData<List<DropDownUi>>()
    private val sourceListDomain: DropDownList = repository.data()

    init {
        liveData.value = mapper.map(sourceListDomain)
    }

    override fun changeCollapseState(id: Int) {
        sourceListDomain.changeCollapseState(id)
        liveData.value = mapper.map(sourceListDomain)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<List<DropDownUi>>) =
        liveData.observe(owner, observer)
}