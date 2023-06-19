package com.example.sneakersnap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    // This is the main activity for the app
    private lateinit var btnWelcomePage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //This is the button to take you to the login page
        btnWelcomePage = findViewById(R.id.btnWelcomePage)
        //This is the intent to take you to the login page
        btnWelcomePage.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

    }
}