package com.clairvoyance.clairvoyance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class DataFieldViewModel : ViewModel(){
    var dataFields = MutableLiveData<MutableList<DataField<*>>>()

    init {
        dataFields.value = mutableListOf()
    }

    // Adds a data field to the list
    fun <T> addDataField(newDataField: DataField<T>) {
        val list = dataFields.value
        list!!.add(newDataField)
        dataFields.postValue(list)
    }

    // Updates a data field
    fun updateDataField(id: UUID, data: Any) {
        val list = dataFields.value
        val dataField = list!!.find {it.id == id}!!
        dataField.data = data
        dataFields.postValue(list)
    }
}