package com.projects.clairvoyance

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class SettingsFragment(private val mainActivity: MainActivity, private val selectedTheme : Int) : Fragment() {
    lateinit var spinnerThemeOptions : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        createSettingsSpinner(view)
        return view
    }

    private fun createSettingsSpinner(view: View) {
        spinnerThemeOptions = view.findViewById(R.id.spinnerText)
        val themeOptions = resources.getStringArray(R.array.theme_settings)
        var adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, themeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerThemeOptions.setAdapter(adapter)
        spinnerThemeOptions.setSelection(selectedTheme)
        spinnerThemeOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("Settings Buttons","Pressed" + themeOptions.get(position))
                val lastTheme : Int = selectedTheme
                mainActivity.selectTheme(position)
                if (lastTheme != position) {
                    mainActivity.fragmentNavigation(R.id.nav_settings)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        Log.d("Settings Buttons","Spinner Generated")
    }
}