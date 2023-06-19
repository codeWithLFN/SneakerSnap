package com.example.sneakersnap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SneakerCategories : AppCompatActivity() {

    // Define the categories array
    private val categories = listOf(
        "Nike", "Adidas", "Jordan", "Yeezy", "New Balance", "Asics",
        "Puma", "Reebok", "Vans", "Converse", "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sneaker_categories)

        // Find the recyclerView_SneakerCategories view and set the adapter and layout manager
        val recyclerViewSneakerCategories = findViewById<RecyclerView>(R.id.recyclerView_SneakerCategories)
        recyclerViewSneakerCategories.adapter = SneakerCategoriesAdapter(categories)
        recyclerViewSneakerCategories.layoutManager = LinearLayoutManager(this)

        // Find the back button and set the click listener
        val btnArrowLeft = findViewById<ImageButton>(R.id.btn_arrowleft)
        btnArrowLeft.setOnClickListener {
            // Go back to the previous activity
            finish()
        }
    }

    // Define the adapter for the recyclerViewSneakerCategories
    private inner class SneakerCategoriesAdapter(val categories: List<String>) :
        RecyclerView.Adapter<SneakerCategoriesAdapter.SneakerCategoriesViewHolder>() {

        // Define the SneakerCategoriesViewHolder and set the click listener
        inner class SneakerCategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val categoryName: TextView = itemView.findViewById<TextView>(R.id.category_name)

            init {
                // Handle category clicks
                itemView.setOnClickListener {
                    val category = categories[adapterPosition]
                    Toast.makeText(this@SneakerCategories, "Clicked on $category", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Create the ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SneakerCategoriesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_item_sneaker_category, parent, false)
            return SneakerCategoriesViewHolder(view)
        }

        // Bind the ViewHolder
        override fun onBindViewHolder(holder: SneakerCategoriesViewHolder, position: Int) {
            val category = categories[position]
            holder.categoryName.text = category
        }

        // Return the size of the categories array
        override fun getItemCount(): Int {
            return categories.size
        }
    }
}
