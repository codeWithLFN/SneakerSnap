package com.example.sneakersnap

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView



class Homepage : AppCompatActivity() {

    // This is the homepage activity for the app
    private lateinit var addSneakerButton: Button
    private lateinit var totalSneakersText: TextView
    private lateinit var bottomNavView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Initialize the add sneaker button and total sneakers text
        addSneakerButton = findViewById(R.id.btnAddSneaker)
        totalSneakersText = findViewById(R.id.totalSneakersText)

        // Set the on click listener for the add sneaker button
        addSneakerButton.setOnClickListener {
            // Perform action when add sneaker button is clicked
            startActivity(Intent(this, AddNewSneaker::class.java))
        }


        // Initialize and set the bottom navigation view
        bottomNavView = findViewById(R.id.bottomNavigationView)

        // Set the bottom navigation view to be selected when the user is on the homepage
        bottomNavView.selectedItemId = R.id.menu_home

        // Set the onNavigationItemSelectedListener for the bottom navigation view
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Do nothing because the user is already on the homepage
                    true
                }
                R.id.menu_collection -> {
                    // Start the search activity
                    startActivity(Intent(this, SneakerCollection::class.java))
                    true
                }
                R.id.menu_goal -> {
                    // Start the goal activity
                    startActivity(Intent(this, SetSneakerGoal::class.java))
                    true
                }
                R.id.menu_achievements -> {
                    // Start the achievements activity
                    startActivity(Intent(this, Achievements::class.java))
                    true
                }
                R.id.menu_profile -> {
                    // Start the profile activity
                    startActivity(Intent(this, Profile::class.java))
                    true
                }
                else -> false
            }
        }
    }
}