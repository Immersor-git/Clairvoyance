package com.clairvoyance.clairvoyance

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class DatabaseManager(private val appViewModel: AppViewModel) {
    private var database: FirebaseFirestore
    private var accountManager : AccountManager
    private var user : UserAccount
// ...
    init {
        database = Firebase.firestore
        accountManager = appViewModel.accountManager;
        user = UserAccount()
    }

    fun loadUser(userID : String, callback: (UserAccount?) -> Unit) {
        database.collection("Users").document(userID).get().addOnSuccessListener { documentSnapshot : DocumentSnapshot ->
            Log.d("Database Login","Successfully found account")
            if (documentSnapshot.data == null) {
                callback(null)
            }
            else {
                user = documentSnapshot.toObject<UserAccount>()!!;
                Log.d("Database Login", "Logged in! User Data: [${user.toString()}]")
                callback(user)
            }
        }
        .addOnFailureListener {
            callback(UserAccount())
        }
    }

    fun writeDoc(docReference : String, data : Any) {
        Log.d("Database","Attempting to write!")
        database.document(docReference).set(data).addOnFailureListener {
            Log.d("Database",it.toString());
        }
    }

    fun saveTask(task : Task, callback : ()->Unit = {}) {
        val user = accountManager.user
        if (user.userID != "X") {
            database.collection("Users").document(user.userID).collection("Tasks").document(task.id).set(FirebaseTask(task))
        }
    }
    fun saveTemplate(template : TaskTemplate, callback : ()->Unit = {}) {
        val user = accountManager.user
        if (user.userID != "X") {
            database.collection("Users").document(user.userID).collection("Templates").document(template.id).set(FirebaseTemplate(template))
        }
    }
    fun getTemplates(callback : (List<TaskTemplate>) -> Unit) {
        val user = accountManager.user
        database.collection("Users").document(user.userID).collection("Templates").get().addOnSuccessListener { documents ->
            val tempList = ArrayList<TaskTemplate>()
            for (s in documents) {
                tempList.add(FirebaseTemplate.asTemplate(s.toObject<FirebaseTemplate>()))
            }
            callback(tempList)
        }
    }
    fun archiveTask(task : Task, callback : ()->Unit = {}) {
        val user = accountManager.user
        if (user.userID != "X") {
            database.collection("Users").document(user.userID).collection("Tasks").document(task.id).delete()
            database.collection("Users").document(user.userID).collection("TaskArchive").document(task.id).set(FirebaseTask(task))
        }
    }

    fun deleteTask(task: Task) {
        val user = accountManager.user
        if (user.userID != "X") {
            database.collection("Users").document(user.userID).collection("TaskArchive").document(task.id).delete()
        }
    }

    fun getTaskArchive(callback : (List<Task>) -> Unit) {
        val user = accountManager.user
        database.collection("Users").document(user.userID).collection("TaskArchive").get().addOnSuccessListener { documents ->
            val archiveList = ArrayList<Task>()
            for (s in documents) {
                archiveList.add(FirebaseTask.asTask(s.toObject<FirebaseTask>()))
            }
            callback(archiveList)
        }
    }
    fun writeUserData(user : UserAccount) {
        database.collection("Users").document(user.userID).set(user)
    }

    fun getTasks(callback : (List<Task>) -> Unit) {
        val user = accountManager.user
        database.collection("Users").document(user.userID).collection("Tasks").get().addOnSuccessListener { documents ->
            val taskList = ArrayList<Task>()
            for (s in documents) {
                taskList.add(FirebaseTask.asTask(s.toObject<FirebaseTask>()))
            }
            callback(taskList)
        }

    }
}