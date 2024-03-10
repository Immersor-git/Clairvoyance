package com.projects.clairvoyance

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources.Theme
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.projects.clairvoyance.databinding.ActivityMainBinding


class MainActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding
    private var currentTheme : Int = 2

    override fun getLayoutInflater(): LayoutInflater {
        val inflater = super.getLayoutInflater()
        val contextThemeWrapper: Context = androidx.appcompat.view.ContextThemeWrapper(
            applicationContext,
            getCustomTheme()
        )
        return inflater.cloneInContext(contextThemeWrapper)
        //return super.getLayoutInflater()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val contextThemeWrapper: Context = androidx.appcompat.view.ContextThemeWrapper(
            applicationContext,
            getCustomTheme()
        )
        val newLayoutInflater = layoutInflater.cloneInContext(contextThemeWrapper)
        binding = ActivityMainBinding.inflate(newLayoutInflater)

        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            onNavigationItemSelected(item)
            true
        }

        fragmentManager = supportFragmentManager
        openFragment(ToDoFragment())

        //binding.fab.setOnClickListener()

        onBackPressedDispatcher.addCallback(this /* lifecycle owner */) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                finish()
            }
        }

        if (getFlag("changingTheme") == 1) {
            setFlag("changingTheme", 0)
            fragmentNavigation(R.id.nav_settings)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return fragmentNavigation(item.itemId)
    }

    fun fragmentNavigation(item : Int): Boolean {
        when(item){
            R.id.bottom_todo -> openFragment(ToDoFragment())
            R.id.bottom_calendar -> openFragment(CalendarFragment())
            R.id.nav_home -> openFragment(ToDoFragment())
            R.id.nav_account -> openFragment(AccountFragment())
            R.id.nav_archive -> openFragment(ArchiveFragment())
            R.id.nav_help -> openFragment(HelpFragment())
            R.id.nav_settings-> openFragment(SettingsFragment())

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
    val themes = arrayOf<Int>(R.style.DarkTheme,R.style.LightTheme)
    fun getCustomTheme() : Int {
        return themes[loadTheme()]
    }
    fun selectTheme(themeIndex : Int) {
        val previousTheme = loadTheme()
        if (themeIndex != previousTheme) {
            val themeOptions = resources.getStringArray(R.array.theme_settings)
            saveTheme(themeIndex)
            setFlag("changingTheme", 1)
            refresh()
        }
        //this.theme = "@style/"+themeOptions[themeIndex]
        /*when(themeIndex){
            0 -> applicationContext.setTheme(themes[themeIndex])
            1 -> applicationContext.setTheme(themes[themeIndex])
        }*/
        Log.d("Main Activity", "Set Theme: "+getCustomTheme())

    }
    fun saveTheme(themeIndex : Int) {
        val preferences : SharedPreferences = getSharedPreferences("PREFERENCE_AppTheme",MODE_PRIVATE)
        val editor : SharedPreferences.Editor = preferences.edit()
        editor.putInt("themeIndex", themeIndex)
        editor.apply()
    }
    fun loadTheme() : Int {
        val preferences : SharedPreferences = getSharedPreferences("PREFERENCE_AppTheme",MODE_PRIVATE)
        return preferences.getInt("themeIndex", 0)
    }
    fun setFlag(flag: String,flagValue : Int ) {
        val preferences : SharedPreferences = getSharedPreferences("MAIN_FLAGS",MODE_PRIVATE)
        val editor : SharedPreferences.Editor = preferences.edit()
        editor.putInt(flag,flagValue)
        editor.apply()
    }
    fun getFlag(flag: String) : Int {
        val preferences : SharedPreferences = getSharedPreferences("MAIN_FLAGS",MODE_PRIVATE)
        return preferences.getInt(flag, 0)
    }

    fun refresh() {
        recreate()
    }
}