package com.clairvoyance.clairvoyance

import androidx.annotation.Keep

@Keep
data class UserAccount(
    val userID: String = "X",
    val email : String = "",
    val password : String = ""
) {
    constructor() : this("", "", "")
    @Override
    override fun toString() : String {
        return "userID: ${userID} , email: ${email} , password: ${password}"
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "user_id" to userID,
            "email" to email,
            "password" to password
        )
    }
}