package com.clairvoyance.clairvoyance

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.clairvoyance.clairvoyance.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalTime

class TaskSheet(private var task : Task?) : BottomSheetDialogFragment(), DataFieldClickListener {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel : TaskViewModel
    private lateinit var dataFieldViewModel: DataFieldViewModel
    private lateinit var dataFieldViewModelFactory: DataFieldViewModelFactory
    private lateinit var dataFields : MutableLiveData<MutableList<DataField<*>>>
    private var endTime: LocalTime? = null
    private var startTime: LocalTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if (task != null) {
            dataFields = task!!.dataFields
            val editable = Editable.Factory.getInstance()

            binding.taskTitle.text = "Edit Task"
            binding.name.text = editable.newEditable(task!!.name)
            binding.desc.text = editable.newEditable(task!!.desc)

            if (task!!.endTime != null) {
                endTime = task!!.endTime
                binding.endTimeButton.text = String.format("%02d:%02d", endTime!!.hour, endTime!!.minute)
            }

            if (task!!.startTime != null) {
                startTime = task!!.startTime
                binding.startTimeButton.text = String.format("%02d:%02d", startTime!!.hour, startTime!!.minute)
            }

        } else {
            binding.taskTitle.text = "New Task"
            dataFields = MutableLiveData<MutableList<DataField<*>>>()
            dataFields.value = mutableListOf()
        }

        taskViewModel = ViewModelProvider(activity)[TaskViewModel::class.java]
        dataFieldViewModelFactory = DataFieldViewModelFactory(dataFields)
        dataFieldViewModel = ViewModelProvider(this, dataFieldViewModelFactory)[DataFieldViewModel::class.java]

        setRecyclerView()

        binding.saveButton.setOnClickListener {
            saveAction()
            addDataField()
        }

        binding.startTimeButton.setOnClickListener {
            openStartTimePicker()
        }

        binding.endTimeButton.setOnClickListener {
            openEndTimePicker()
        }

        binding.addDataFieldButton.setOnClickListener {
            addDataField()
        }

    }

    private fun openStartTimePicker() {
        if (startTime == null) startTime = LocalTime.now()

        val listener = TimePickerDialog.OnTimeSetListener {_, selectedHour, selectedMinute ->
            startTime = LocalTime.of(selectedHour, selectedMinute)
            binding.startTimeButton.text = String.format("%02d:%02d", startTime!!.hour, startTime!!.minute)
        }

        val dialog = TimePickerDialog(activity, listener, startTime!!.hour, startTime!!.minute, false)
        dialog.setTitle("Select a time:")
        dialog.show()
    }

    private fun openEndTimePicker() {
        if (endTime == null) endTime = LocalTime.now()

        val listener = TimePickerDialog.OnTimeSetListener {_, selectedHour, selectedMinute ->
            endTime = LocalTime.of(selectedHour, selectedMinute)
            binding.endTimeButton.text = String.format("%02d:%02d", endTime!!.hour, endTime!!.minute)
        }

        val dialog = TimePickerDialog(activity, listener, endTime!!.hour, endTime!!.minute, false)
        dialog.setTitle("Select a time:")
        dialog.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)

        val dataFieldList = resources.getStringArray(R.array.datafields)
        val arrayAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dataFieldList)
        binding.dataFieldSpinner.adapter = arrayAdapter

        return binding.root
    }

    private fun saveAction() {
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()

        if (task == null) {
            val newTask = Task(name, desc, startTime, endTime, null, null)
            newTask.addDataFields(dataFields)
            taskViewModel.addTask(newTask)
        } else {
            taskViewModel.updateTask(task!!.id, name, desc, startTime, endTime, null, dataFields)
        }

        binding.name.setText("")
        binding.desc.setText("")

        dismiss()
    }

    private fun addDataField() {
        val typeString = binding.dataFieldSpinner.selectedItem.toString()

        lateinit var dataType: DataType
        lateinit var dataField: DataField<*>

        when (typeString) {
            "TEXT" -> {
                dataType = DataType.TEXT
                dataField = DataField<String>(dataType, null)
                dataFieldViewModel.addDataField(dataField)
            }
            "NUMBER" -> {
                dataType = DataType.NUMBER
                dataField = DataField<Int>(dataType, null)
                dataFieldViewModel.addDataField(dataField)
            }
            "DATE" -> {
                dataType = DataType.DATE
                dataField = DataField<LocalDate>(dataType, null)
                dataFieldViewModel.addDataField(dataField)
            }
            else -> {
                println("Unsupported data type")
            }
        }
    }

    private fun setRecyclerView() {
        val clickListener = this
        dataFieldViewModel.dataFields.observe(this) {
            binding.dataFieldsRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = DataFieldAdapter(it, clickListener)
            }
        }
    }

    override fun <T> editDataField(dataField: DataField<T>) {
        TODO("Not yet implemented")
    }

    override fun <T> removeDataField(dataField: DataField<T>) {
        TODO("Not yet implemented")
    }
}