package com.clairvoyance.clairvoyance

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth

class AccountManager(private val appViewModel: AppViewModel) {
    private var signedIn = false
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authCredential : AuthCredential
    var user : UserAccount
    private lateinit var databaseManager: DatabaseManager

    init {
        databaseManager = appViewModel.databaseManager
        user = UserAccount()
    }

    fun appViewModelLoaded() {
        databaseManager = appViewModel.databaseManager
    }
    fun loadAccount() {
        databaseManager.loadUser(firebaseAuth.uid!!) {
            if (it != null) {
                user = it
            }
        }
    }

    fun safeInitializeAccount(userID : String, email : String, password : String) {
        val tempUser : UserAccount = UserAccount(userID, email, password)
        databaseManager.writeUserData(tempUser);
    }

    fun loginAccount(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signedIn = true
                databaseManager.loadUser(it.result.user!!.uid) { useraccount ->
                    if (useraccount == null) {
                        user = UserAccount(it.result.user!!.uid, email, password)
                        databaseManager.writeUserData(user)
                    } else {
                        user = useraccount
                        appViewModel.taskViewModel.getUserTasks()
                        appViewModel.taskViewModel.getTemplates()
                        appViewModel.taskViewModel.getArchivedTasks()
                        if (user.userID == "X") {
                            safeInitializeAccount(it.result.user!!.uid, email, password)
                        }
                    }
                }
            }
            callback(it.isSuccessful)
        }
    }

    fun registerAccount(email : String, password : String, callback: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signedIn = true
                val dbManager : DatabaseManager = appViewModel.databaseManager
                user = UserAccount(it.result.user!!.uid,email, password)
                dbManager.writeUserData(user)
            }
            callback(it.isSuccessful)
        }
    }
    fun signOut() {
        signedIn = false
        firebaseAuth.signOut()
    }
    fun isSignedIn() : Boolean {
        return signedIn;
    }
}