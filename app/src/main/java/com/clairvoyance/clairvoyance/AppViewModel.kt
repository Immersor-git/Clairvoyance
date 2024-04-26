package com.clairvoyance.clairvoyance

import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    lateinit var accountManager : AccountManager;
    lateinit var taskViewModel : TaskViewModel;
    init {

    }
}