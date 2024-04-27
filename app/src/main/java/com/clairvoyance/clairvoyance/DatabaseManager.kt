package com.clairvoyance.clairvoyance

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseManager(private val appViewModel: AppViewModel) {
    private lateinit var database: DatabaseReference
// ...
    init {
        database = Firebase.database.reference
    }

    fun writeDoc(docReference : String, data : Any) {
        database.child(docReference).setValue(data).addOnFailureListener {
            Log.d("Database",it.toString());
        }
    }
}