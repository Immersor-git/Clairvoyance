package com.projects.clairvoyance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.ImageButton
import android.util.Log

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        val appState = TaskState(false);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity","Loaded");
        val tvMenuTest = findViewById<TextView>(R.id.tvMenuTest);
        val menuButton = findViewById<ImageButton>(R.id.ibHamburgerMenu);
        menuButton.setOnClickListener { view ->
            fun toggleMenu(view: View) {
                Log.d("MainActivity", "Clicked!");
                appState.viewingTask = !appState.viewingTask;
                if (appState.viewingTask) {
                    tvMenuTest.setVisibility(View.VISIBLE);
                } else {
                    tvMenuTest.setVisibility(View.INVISIBLE);
                }
            }
            toggleMenu(view);
        }
    }
}