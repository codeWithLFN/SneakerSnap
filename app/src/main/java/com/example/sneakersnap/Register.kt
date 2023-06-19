package com.example.sneakersnap

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.btnRegister1)
        usernameEditText = findViewById(R.id.registerUsername)
        passwordEditText = findViewById(R.id.registerPassword)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter a valid username and password", Toast.LENGTH_SHORT).show()
            } else {
                mAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val message = "Registration successful: Username=$username, Password=$password"
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                            val currentUser = mAuth.currentUser
                            val user = hashMapOf(
                                "username" to username,
                                "password" to password
                            )

                            if (currentUser != null) {
                                db.collection("users").document(currentUser.uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        val intent = Intent(this, Login::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this,
                                            "Failed to add user to Firestore",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            val exception = task.exception
                            Toast.makeText(this, exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
