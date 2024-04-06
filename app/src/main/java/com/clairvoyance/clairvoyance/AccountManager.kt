package com.clairvoyance.clairvoyance

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth

class AccountManager {
    private var signedIn = false
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authCredential : AuthCredential

    private var email : String = ""
    private var password : String = ""



    fun setCredentials(email : String, password : String) {
        this.email = email
        this.password = password
    }
    fun loginAccount(callback: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signedIn = true
            }
            callback(it.isSuccessful)
        }
    }

    fun registerAccount(callback: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signedIn = true
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