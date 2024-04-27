package com.clairvoyance.clairvoyance

import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    var accountManager : AccountManager;
    var taskViewModel : TaskViewModel;
    var databaseManager : DatabaseManager;
    private var testString = "Test Undedited";
    init {
        accountManager = AccountManager(this)
        taskViewModel = TaskViewModel(this)
        databaseManager = DatabaseManager(this)
    }

    fun setTestString (s : String) {
        this.testString = s;
    }

    fun getTestString() : String {
        return this.testString;
    }
}