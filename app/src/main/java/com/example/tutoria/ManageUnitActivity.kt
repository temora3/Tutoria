package com.example.tutoria

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ManageUnitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_units)

        // Set up the Spinner with the list of units
        val unitList = listOf("Cryptography", "Server Administration", "Database Administration")
        val spinner = findViewById<Spinner>(R.id.unitSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, unitList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Handle the button to choose a PDF file
        findViewById<Button>(R.id.selectPdfButton).setOnClickListener {
            pickPdfFile()
        }

        // Handle the upload button (No action for now)
        findViewById<Button>(R.id.uploadPdfButton).setOnClickListener {
            // This is where the upload logic would go, but currently it's not doing anything
        }
    }

    private fun pickPdfFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val selectedPdfUri: Uri = data?.data!!
            // Display the selected file name
            val fileName = selectedPdfUri.lastPathSegment
            findViewById<TextView>(R.id.selectedFileText).text = "Selected file: $fileName"
        }
    }
}
