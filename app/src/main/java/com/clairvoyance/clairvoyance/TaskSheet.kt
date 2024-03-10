package com.clairvoyance.clairvoyance

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.clairvoyance.clairvoyance.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

class TaskSheet(var task : Task?) : BottomSheetDialogFragment(), DataFieldClickListener {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel : TaskViewModel
    private var endTime: LocalTime? = null
    private var startTime: LocalTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if (task != null) {
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
        }

        taskViewModel = ViewModelProvider(activity)[TaskViewModel::class.java]

        binding.saveButton.setOnClickListener {
            saveAction()
        }

        binding.startTimeButton.setOnClickListener {
            openStartTimePicker()
        }

        binding.endTimeButton.setOnClickListener {
            openEndTimePicker()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
            taskViewModel.addTask(newTask)
        } else {
            taskViewModel.updateTask(task!!.id, name, desc, startTime, endTime, null)
        }

        binding.name.setText("")
        binding.desc.setText("")

        dismiss()
    }

    override fun editDataField(task: Task) {
        TODO("Not yet implemented")
    }

    override fun removeDataField(task: Task) {
        TODO("Not yet implemented")
    }
}