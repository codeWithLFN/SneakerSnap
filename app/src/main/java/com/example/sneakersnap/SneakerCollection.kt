package com.example.sneakersnap

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class SneakerCollection : AppCompatActivity() {
    private lateinit var addSneakerButton: Button
    private lateinit var categoryButton: Button
    private lateinit var listViewSneaker: ListView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: SneakerAdapter
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sneaker_collection)

        addSneakerButton = findViewById(R.id.btnAddSneaker)
        categoryButton = findViewById(R.id.btnCategory)
        listViewSneaker = findViewById(R.id.listViewSneaker)
        firestore = FirebaseFirestore.getInstance()

        bottomNavView = findViewById(R.id.bottomNavigationView)
        bottomNavView.selectedItemId = R.id.menu_collection

        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, Homepage::class.java))
                    true
                }
                R.id.menu_collection -> {
                    true
                }
                R.id.menu_goal -> {
                    startActivity(Intent(this, SetSneakerGoal::class.java))
                    true
                }
                R.id.menu_achievements -> {
                    startActivity(Intent(this, Achievements::class.java))
                    true
                }
                R.id.menu_profile -> {
                    startActivity(Intent(this, Profile::class.java))
                    true
                }
                else -> false
            }
        }

        addSneakerButton.setOnClickListener {
            startActivity(Intent(this, AddNewSneaker::class.java))
            finish()
        }

        categoryButton.setOnClickListener {
            startActivity(Intent(this, SneakerCategories::class.java))
            finish()
        }

        adapter = SneakerAdapter(this, mutableListOf())
        listViewSneaker.adapter = adapter

        fetchData()
    }

    private fun fetchData() {
        firestore.collection("sneakers")
            .get()
            .addOnSuccessListener { documents ->
                val sneakers = documents.toObjects(Sneaker::class.java)
                adapter.updateSneakers(sneakers)
            }
            .addOnFailureListener { exception ->
                // Handle error
                exception.printStackTrace()
            }
    }

    fun backToHomepage(view: View) {
        startActivity(Intent(this, Homepage::class.java))
        finish()
    }
}
