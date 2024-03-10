package com.clairvoyance.clairvoyance

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.clairvoyance.clairvoyance.databinding.DataFieldItemBinding

class DataFieldViewHolder(
    private val context: Context,
    private val binding: DataFieldItemBinding,
    private val clickListener: DataFieldClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bindDataField(dataField: DataField<*>) {
    }
}