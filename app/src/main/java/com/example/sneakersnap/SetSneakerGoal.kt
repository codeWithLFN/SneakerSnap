package com.example.sneakersnap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SetSneakerGoal : AppCompatActivity() {

    private lateinit var editGoal: EditText
    private lateinit var btnSetGoal: Button
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var auth: FirebaseAuth

    // Progress indicators
    private lateinit var progressSneakerGoal: ProgressBar
    private lateinit var textProgressPercentage: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_sneaker_goal)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        editGoal = findViewById(R.id.editGoal)
        btnSetGoal = findViewById(R.id.btnSetGoal)

        // Progress indicators
        progressSneakerGoal = findViewById(R.id.progressSneakerGoal)
        textProgressPercentage = findViewById(R.id.textProgressPercentage)

        btnSetGoal.setOnClickListener {
            // Set the sneaker goal
            val goal = editGoal.text.toString()
            if (goal.isNotEmpty()) {
                val firestore = FirebaseFirestore.getInstance()
                val userId = auth.currentUser?.uid
                val goalData = hashMapOf(
                    "goal" to goal.toDouble(),
                    "userId" to userId
                )
                firestore.collection("goals")
                    .add(goalData)
                    .addOnSuccessListener {
                        // Goal saved successfully
                        Toast.makeText(this, "Goal saved successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Homepage::class.java))
                    }
                    .addOnFailureListener {
                        // Error saving goal
                        Toast.makeText(this, "Error saving goal", Toast.LENGTH_SHORT).show()
                    }
                calculateGoalCompletion()
            }
        }

        // Initialize and set the bottom navigation view
        bottomNavView = findViewById(R.id.bottomNavigationView)

        // Set the bottom navigation view to be selected when the user is on the Goals page
        bottomNavView.selectedItemId = R.id.menu_goal

        // Set the onNavigationItemSelectedListener for the bottom navigation view
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Start the homepage activity
                    startActivity(Intent(this, Homepage::class.java))
                    true
                }

                R.id.menu_collection -> {
                    // Start the search activity
                    startActivity(Intent(this, SneakerCollection::class.java))
                    true
                }

                R.id.menu_goal -> {
                    // Do nothing because the user is already on the Goals page
                    true
                }

                R.id.menu_achievements -> {
                    // Start the search activity
                    startActivity(Intent(this, Achievements::class.java))
                    true
                }

                R.id.menu_profile -> {
                    // Start the search activity
                    startActivity(Intent(this, Profile::class.java))
                    true
                }

                else -> false
            }
        }

        calculateGoalCompletion()
    }

    private fun calculateGoalCompletion() {
        val userId = auth.currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("goals")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val goalDocument = querySnapshot.documents.firstOrNull()
                    val goal = goalDocument?.get("goal") as? Double
                    if (goal != null) {
                        val progress = when {
                            goal in (1.0..5.0) -> calculateProgress(1.0, 5.0, goal)
                            goal > 6.0 && goal <= 10.0 -> calculateProgress(6.0, 10.0, goal)
                            else -> 0
                        }
                        progressSneakerGoal.progress = progress
                        val percentageText = "$progress%"
                        textProgressPercentage.text = percentageText
                    } else {
                        progressSneakerGoal.progress = 0
                        textProgressPercentage.text = "Progression goals 0%"
                    }
                } else {
                    progressSneakerGoal.progress = 0
                    textProgressPercentage.text = "Progression goals 0%"
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error retrieving goal: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun calculateProgress(min: Double, max: Double, value: Double): Int {
        val progressPercentage = ((value - min) / (max - min)) * 100
        return progressPercentage.toInt()
    }
}
