package com.example.sneakersnap

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the buttons and edit texts
        loginButton = findViewById(R.id.btnLogin)
        registerButton = findViewById(R.id.btnRegister)
        usernameEditText = findViewById(R.id.loginUsername)
        passwordEditText = findViewById(R.id.loginPassword)

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                db.collection("users").whereEqualTo("username", username).get().addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val userData = documents.documents[0].toObject(User::class.java)
                        if (userData != null && userData.password == password) {
                            auth.signInWithEmailAndPassword(username, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        val message = "Login successful: Username=$username"
                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, Homepage::class.java))
                                    } else {
                                        val message = "Login failed: ${task.exception?.message}"
                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            val message = "Incorrect username or password"
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val message = "User not found"
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    val message = "Error: ${exception.message}"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }
}
