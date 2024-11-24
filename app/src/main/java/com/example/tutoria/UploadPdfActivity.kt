package com.example.tutoria

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tutoria.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class UploadPdfActivity : AppCompatActivity() {

    private lateinit var storageRef: StorageReference
    private lateinit var selectedPdfUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_pdf)

        val unitName = intent.getStringExtra("unitName") // Get the unit name passed from the previous activity

        // Initialize Firebase Storage reference
        storageRef = FirebaseStorage.getInstance().reference

        findViewById<Button>(R.id.uploadButton).setOnClickListener {
            pickPdfFile()
        }

        findViewById<Button>(R.id.submitButton).setOnClickListener {
            uploadPdf(unitName ?: "")
        }
    }

    private fun pickPdfFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedPdfUri = data?.data!!
            }
        }

    private fun uploadPdf(unitName: String) {
        val pdfRef: StorageReference = storageRef.child("pdfs/$unitName/${UUID.randomUUID()}.pdf")

        pdfRef.putFile(selectedPdfUri)
            .addOnSuccessListener {
                Toast.makeText(this, "PDF uploaded successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Upload failed.", Toast.LENGTH_SHORT).show()
            }
    }
}
