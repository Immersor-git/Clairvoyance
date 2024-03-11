package com.clairvoyance.clairvoyance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle

class DataFieldViewModelFactory(
    private val list: MutableLiveData<MutableList<DataField<*>>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T> ): T {
        return modelClass.getConstructor(MutableLiveData::class.java).newInstance(list)
    }
}