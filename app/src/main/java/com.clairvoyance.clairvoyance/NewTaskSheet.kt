package com.clairvoyance.clairvoyance

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.clairvoyance.clairvoyance.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime
import java.util.UUID

class NewTaskSheet(var task : Task?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel : TaskViewModel
    private var dueTime: LocalTime? = null
    private lateinit var mainActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        mainActivity = activity as MainActivity

        if (task != null) {
            binding.taskTitle.text = "Edit Task"

            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(task!!.name)
            binding.desc.text = editable.newEditable(task!!.desc)

            if (task!!.dueTime != null) {
                dueTime = task!!.dueTime
                updateTimeButtonText()
            }
        } else {
            binding.taskTitle.text = "New task"
        }

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)

        binding.saveButton.setOnClickListener {
            saveAction()
        }

        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
    }

    private fun openTimePicker() {
        if (dueTime == null)
            dueTime = LocalTime.now()

        val listener = TimePickerDialog.OnTimeSetListener{_, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }

        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, false)
        dialog.setTitle("Task Due Time")
        dialog.show()
    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction() {
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()

        if (task == null) {
            val newTask = Task(name, desc, null, null, dueTime, null, null)
            taskViewModel.addTask(newTask)
        } else {
            val taskId = task?.id ?: UUID.randomUUID()
            taskViewModel.updateTask(taskId, name, desc, null, null, dueTime, null)
        }

        binding.name.setText("")
        binding.desc.setText("")

        dismiss()
    }
}
