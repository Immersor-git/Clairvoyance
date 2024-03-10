package com.clairvoyance.clairvoyance

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DataFieldAdapter(
    private val dataFields: List<DataField<*>>
) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return dataFields.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}