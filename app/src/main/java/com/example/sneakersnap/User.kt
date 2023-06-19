package com.example.sneakersnap

//A data class to represent user data
class User(
    private val username: String = "",
    val password: String = ""
) {
    override fun toString(): String {
        return "$username $password"
    }
}
