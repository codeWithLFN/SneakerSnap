package com.example.sneakersnap

class Sneaker(
    val brand: String = "",
    val name: String = "",
    private val category: String = "",
    private val price: String = "",
    private val image: String  = ""
) {
    //add any other fields as necessary
    override fun toString(): String {
        return "$brand $name $category $price $image"
    }
}
