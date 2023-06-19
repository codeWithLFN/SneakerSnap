package com.example.sneakersnap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Achievements : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Get the current user's ID
        val userId = auth.currentUser?.uid

        // Query the Firestore collection to get the user's sneaker count
        firestore.collection("sneakers")
            .whereEqualTo("user", userId)
            .get()
            .addOnSuccessListener { documents ->
                val sneakerCount = documents.size()
                updateAchievements(sneakerCount)
            }
            .addOnFailureListener { exception ->
                // Handle the failure
            }

        // Initialize and set the bottom navigation view
        bottomNavView = findViewById(R.id.bottomNavigationView)

        // Set the bottom navigation view to be selected when the user is on the achievements page
        bottomNavView.selectedItemId = R.id.menu_achievements

        // Set the onNavigationItemSelectedListener for the bottom navigation view
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Start the homepage activity
                    startActivity(Intent(this, Homepage::class.java))
                    true
                }
                R.id.menu_collection -> {
                    // Start the sneaker collection activity
                    startActivity(Intent(this, SneakerCollection::class.java))
                    true
                }
                R.id.menu_goal -> {
                    // Start the goal activity
                    startActivity(Intent(this, SetSneakerGoal::class.java))
                    true
                }
                R.id.menu_achievements -> {
                    // Do nothing because the user is already on the achievements page
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

    private fun updateAchievements(sneakerCount: Int) {
        val tvStarterAchievement = findViewById<TextView>(R.id.tvStarterAchievement)
        val tvCollectorAchievement = findViewById<TextView>(R.id.tvCollectorAchievement)
        val tvPackratAchievement = findViewById<TextView>(R.id.tvPackratAchievement)

        // Update the achievement text based on the sneaker count
        if (sneakerCount >= 1) {
            tvStarterAchievement.text = "Starter: Unlocked"
        }

        if (sneakerCount >= 3) {
            tvCollectorAchievement.text = "Collector: Unlocked"
        }

        if (sneakerCount >= 10) {
            tvPackratAchievement.text = "Packrat: Unlocked"
        }
    }
}
