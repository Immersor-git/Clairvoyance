package com.clairvoyance.clairvoyance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clairvoyance.clairvoyance.databinding.DataFieldItemBinding

class DataFieldAdapter(
    private val dataFields: List<DataField<*>>,
    private val clickListener: DataFieldClickListener
) : RecyclerView.Adapter<DataFieldViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataFieldViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = DataFieldItemBinding.inflate(from, parent, false)
        return DataFieldViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int {
        return dataFields.size
    }

    override fun onBindViewHolder(holder: DataFieldViewHolder, position: Int) {
        holder.bindDataField(dataFields[position])
    }
}