package com.clairvoyance.clairvoyance

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.clairvoyance.clairvoyance.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class  MainActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding;
    private var currentTheme : Int = 2
    private var currentTask: Int = 0;

    override fun getLayoutInflater(): LayoutInflater {  //Overwrites theme on generation of layout
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

        setSupportActionBar(binding.toolbar) //Creates top navbar

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        //binding.bottomNavigation.background = null //Handles clicks on menu
        binding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            onNavigationItemSelected(item)
            true
        }

        fragmentManager = supportFragmentManager
        openFragment(ToDoListFragment())

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
    fun openCamera() {
        val cameraContainer = findViewById<FrameLayout>(R.id.camera_container)
        cameraContainer.visibility = View.VISIBLE
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.camera_container, CameraActivity1())
        fragmentTransaction.commit()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

    }

    fun closeCamera() {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(CameraActivity1())
        fragmentTransaction.commit()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        val cameraContainer = findViewById<FrameLayout>(R.id.camera_container)
        cameraContainer.visibility = View.GONE
    }

    fun fragmentNavigation(item : Int): Boolean { //Loads desired fragment from list
        when(item){
            R.id.bottom_todo -> openFragment(ToDoListFragment())
            R.id.bottom_calendar -> openFragment(MonthlyView())
            R.id.nav_home -> openFragment(ToDoListFragment())
            R.id.nav_account -> openFragment(AccountFragment())
            R.id.nav_archive -> openFragment(ArchiveFragment())
            R.id.nav_help -> openFragment(HelpFragment())
            R.id.nav_settings-> openFragment(SettingsFragment())

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openFragment(fragment: Fragment) { //Opens fragment
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
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

    fun getCurrentTask() : String {
        when (currentTask) {
            0 -> return "task1"
            1 -> return "task2"
            2 -> return "task3"
        }
        return "task2";
    }

    fun setCurrentTask(taskNum : Int) {
        currentTask = taskNum
    }
    fun openTaskFragment() {
        openFragment(ViewTask())
    }
}