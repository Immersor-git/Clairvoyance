package com.clairvoyance.clairvoyance

import android.text.Editable
import androidx.recyclerview.widget.RecyclerView
import com.clairvoyance.clairvoyance.databinding.DataFieldItemBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataFieldViewHolder(
    private val binding: DataFieldItemBinding,
    private val clickListener: DataFieldClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun <T> bindDataField(dataField: DataField<T>) {
        // Handle data differently based on their data types
        if (dataField.data != null) {
            when (dataField.dataType) {
                DataType.TEXT -> {
                    binding.data.setText(dataField.data as String)
                }
                DataType.NUMBER -> {
                    binding.data.setText(dataField.data as Int)
                }
                DataType.DATE -> {
                    val formatter = DateTimeFormatter.ofPattern("MMMM dd")
                    val date = (dataField.data as LocalDate).format(formatter)
                    binding.data.setText(date)
                }
                else -> {
                    println("Unsupported datatype.")
                }
            }
        }
    }
}