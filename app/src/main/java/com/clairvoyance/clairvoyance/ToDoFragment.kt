package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.view.ContextThemeWrapper


//Handles the To Do List page
class ToDoFragment : Fragment() {
    lateinit var mainActivity : MainActivity;
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater { //Applies theme on loading
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
        val view = inflater.inflate(R.layout.fragment_to_do, container, false)

        val arrayAdapter : ArrayAdapter<CharSequence>? =
            getContext()?.let { ArrayAdapter.createFromResource(it, R.array.task_list, android.R.layout.simple_list_item_1) }

        val listView = view.findViewById<ListView>(R.id.lvTaskList) //Propogates list with all available tasks
        listView.adapter = arrayAdapter
        var firstCall = 0
        listView.setOnItemClickListener { parent, view, position, id -> //Click handler for tasks
            Log.d("To Do List","Pressed Item: "+position)
            mainActivity.setCurrentTask(position) //Sets the current task
            mainActivity.openTaskFragment() //Loads the task fragment via mainactivity
        }
        return view
    }
}