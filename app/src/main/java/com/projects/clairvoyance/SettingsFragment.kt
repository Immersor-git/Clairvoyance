package com.projects.clairvoyance

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction


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
        mainActivity = getActivity() as MainActivity
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        createSettingsSpinner(view)
        return view
    }

    private fun createSettingsSpinner(view: View) {
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
                if (firstCall == 0) {
                    firstCall += 1
                    return
                }
                Log.d("Settings Buttons","Pressed" + themeOptions[position])
                //if (position == 0) {
                //    return
                //}
                mainActivity.selectTheme(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        Log.d("Settings Buttons","Spinner Generated")
    }
}