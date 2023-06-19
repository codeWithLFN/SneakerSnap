package com.example.sneakersnap

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class AddNewSneaker : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST_CODE = 2

    private lateinit var addImageButton: Button
    private lateinit var imageView: ImageView
    private lateinit var addSneaker: Button
    private lateinit var sneakerName: EditText
    private lateinit var sneakerPrice: EditText
    private lateinit var sneakerSize: EditText
    private lateinit var sneakerBrand: Spinner
    private lateinit var cancelSneaker: Button
    private lateinit var cameraButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var user: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_sneaker)

        sneakerName = findViewById(R.id.etSneakerName)
        sneakerPrice = findViewById(R.id.etSneakerPrice)
        sneakerSize = findViewById(R.id.etSneakerSize)
        sneakerBrand = findViewById(R.id.spinnerSneakerBrand)
        addSneaker = findViewById(R.id.btnAddSneaker)
        cancelSneaker = findViewById(R.id.btnCancel)
        addImageButton = findViewById(R.id.btnAddImage)
        cameraButton = findViewById(R.id.btnCamera)
        imageView = findViewById(R.id.imageView)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        user = firebaseAuth.currentUser?.uid.toString()

        addImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }
        }

        addSneaker.setOnClickListener {
            val sneakerName = sneakerName.text.toString()
            val sneakerPrice = sneakerPrice.text.toString()
            val sneakerSize = sneakerSize.text.toString()
            val sneakerBrand = sneakerBrand.selectedItem.toString()

            if (imageView.tag != null) {
                val imageUri = Uri.parse(imageView.tag.toString())
                val compressedImage = compressImage(MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri))
                if (compressedImage != null) {
                    uploadImage(compressedImage) { imageUrl ->
                        val sneakerImage = imageUrl ?: ""

                        val sneaker = hashMapOf(
                            "name" to sneakerName,
                            "price" to sneakerPrice,
                            "size" to sneakerSize,
                            "brand" to sneakerBrand,
                            "image" to sneakerImage,
                            "user" to user
                        )

                        firestore.collection("sneakers")
                            .add(sneaker)
                            .addOnSuccessListener {
                                finish()
                            }
                            .addOnFailureListener {
                                // handle error
                                finish()
                            }
                    }
                } else {
                    // Failed to compress image
                    // Handle the error
                    finish()
                }
            } else {
                // No image selected
                // Show an error message or handle it according to your app's requirements
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        cancelSneaker.setOnClickListener {
            startActivity(Intent(this, SneakerCollection::class.java))
            finish()
        }
    }

    // Camera
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data?.data != null) {
            val imageUri = data.data
            imageView.setImageURI(imageUri)
            imageView.tag = imageUri.toString()
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data?.extras != null) {
            val imageBitmap = data.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            val imageUri = getImageUriFromBitmap(imageBitmap)
            imageView.tag = imageUri.toString()
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val imagePath = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "title", null)
        return Uri.parse(imagePath)
    }



    private fun compressImage(bitmap: Bitmap): Bitmap? {
        val maxHeight = 1280.0f
        val maxWidth = 1280.0f

        val scale = when {
            bitmap.width > bitmap.height -> maxHeight / bitmap.height
            else -> maxWidth / bitmap.width
        }

        val scaledWidth = (bitmap.width * scale).toInt()
        val scaledHeight = (bitmap.height * scale).toInt()

        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
    }


    private fun uploadImage(bitmap: Bitmap, onComplete: (String?) -> Unit) {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("sneaker_images").child(user).child("${System.currentTimeMillis()}.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString())
            }.addOnFailureListener { exception ->
                onComplete(null)
                // Handle any errors
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            onComplete(null)
            // Handle any errors

        }
    }
}

