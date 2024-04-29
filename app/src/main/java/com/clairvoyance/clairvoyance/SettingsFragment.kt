package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment

//Handles the settings menu
class SettingsFragment() : Fragment() {
    lateinit var spinnerThemeOptions : Spinner
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
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val btnNotification = view.findViewById<Button>(R.id.btnNotification)
        btnNotification.setOnClickListener {
            onNotificationClick(view)
        }
        createSettingsSpinner(view)
        return view
    }

    private fun createSettingsSpinner(view: View) { //Creates a dropdown menu for selecting a theme.
        spinnerThemeOptions = view.findViewById(R.id.spinnerText)
        val themeOptions = resources.getStringArray(R.array.theme_settings)
        //val themeOptions = listOf(themeOptionsRaw[mainActivity.loadTheme()]) + themeOptionsRaw
        var adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, themeOptions)
        spinnerThemeOptions.setAdapter(adapter)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) //simple_spinner_dropdown_item
        spinnerThemeOptions.setSelection(mainActivity.loadTheme())
        var firstCall = 0;
        spinnerThemeOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (firstCall == 0) { //Ignores first click call (happens when menu loads)
                    firstCall += 1
                    return
                }
                Log.d("Settings Buttons","Pressed" + themeOptions[position])
                mainActivity.selectTheme(position) //Sets the theme in the main activity

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }



        Log.d("Settings Buttons","Spinner Generated")
    }

    fun onNotificationClick(view: View) {
        // Navigate to the "Task Archive" page
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, NotificationSettingsFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}




