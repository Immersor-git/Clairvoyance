package com.clairvoyance.clairvoyance

import android.content.Context
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
import java.util.Locale

class SettingsFragment : Fragment() {

    lateinit var spinnerThemeOptions: Spinner
    lateinit var spinnerLanguage: Spinner
    lateinit var mainActivity: MainActivity

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        mainActivity = getActivity() as MainActivity

        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
        return inflater.cloneInContext(contextThemeWrapper)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        createSettingsSpinners(view)
        return view
    }

    private fun createSettingsSpinners(view: View) {
        // Theme Spinner
        spinnerThemeOptions = view.findViewById(R.id.spinnerText)
        val themeOptions = resources.getStringArray(R.array.theme_settings)
        val themeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, themeOptions)
        spinnerThemeOptions.adapter = themeAdapter
        spinnerThemeOptions.setSelection(mainActivity.loadTheme(), false)

        spinnerThemeOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != mainActivity.loadTheme()) {
                    Log.d("Settings", "Theme changed to: ${themeOptions[position]}")
                    mainActivity.selectTheme(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }

        spinnerLanguage = view.findViewById(R.id.spinnerLanguage)
        val languageOptions = resources.getStringArray(R.array.language_options)
        val languageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languageOptions)
        spinnerLanguage.adapter = languageAdapter
        spinnerLanguage.setSelection(getCurrentLanguagePosition(), false)

        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguageCode = when (position) {
                    0 -> "en" // English
                    1 -> "es" // Spanish
                    2 -> "fr" // French
                    // Add more cases for other languages if needed
                    else -> "en" // Default to English
                }
                setLanguage(selectedLanguageCode)
                Log.d("Settings", "Language changed to: $selectedLanguageCode")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }
    }

    private fun getCurrentLanguagePosition(): Int {
        // Get the currently selected language code from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val currentLanguage = sharedPreferences.getString("language", "en") // Default to English if not found
        // Find the position of the current language in the languageOptions array
        return resources.getStringArray(R.array.language_options).indexOf(currentLanguage)
    }

    private fun setLanguage(languageCode: String) {
        // Save the selected language code to SharedPreferences for persistence
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language", languageCode)
        editor.apply()

        // Set the app's language based on the selected language code
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        // Restart the activity to apply language changes
        requireActivity().recreate()
    }

}

