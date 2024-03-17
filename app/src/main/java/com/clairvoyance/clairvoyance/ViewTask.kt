package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper

//Displays task based on current state of app
class ViewTask : Fragment() {
    lateinit var mainActivity : MainActivity;
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        mainActivity = getActivity() as MainActivity


        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
        return inflater.cloneInContext(contextThemeWrapper)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val currentTask = mainActivity.getCurrentTask() //Request selected task
        val taskData = getTaskList(currentTask) //Convert task to its associated string array

        val view = inflater.inflate(R.layout.fragment_view_task, container, false)

        val tvTaskTitle = view.findViewById<TextView>(R.id.tvTaskTitle) //Gets all Text tags in the task template
        val tvTaskCategory = view.findViewById<TextView>(R.id.tvTaskCategory)
        val tvTaskTime = view.findViewById<TextView>(R.id.tvTaskTime)
        val tvTaskReminder = view.findViewById<TextView>(R.id.tvTaskReminder)
        val tvTaskIsChecklist = view.findViewById<TextView>(R.id.tvTaskIsChecklist)

        tvTaskTitle.setText(taskData[0]) //Sets each tag to be the specified value associated with the task (name, time, checklist, etc)
        tvTaskCategory.setText(taskData[1])
        tvTaskTime.setText(taskData[2])
        tvTaskReminder.setText(taskData[3])
        tvTaskIsChecklist.setText(taskData[4])

        return view
    }


    fun getTaskList(taskName : String) : Array<String> {
        when(taskName) {
            "task1" -> return resources.getStringArray(R.array.task1)
            "task2" -> return resources.getStringArray(R.array.task2)
            "task3" -> return resources.getStringArray(R.array.task3)
        }
        return resources.getStringArray(R.array.task1);
    }
}