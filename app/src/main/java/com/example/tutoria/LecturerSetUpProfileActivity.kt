package com.example.tutoria

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tutoria.databinding.ActivitySetupLecturerProfileBinding
import com.example.tutoria.mvm.Lecturer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LecturerSetUpProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupLecturerProfileBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupLecturerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Get the user ID passed from the signup activity
        userId = intent.getStringExtra("USER_ID") ?: ""

        // On save button click
        binding.buttonSaveProfile.setOnClickListener {
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val dateOfBirth = binding.editTextDateOfBirth.text.toString()
            val email = binding.editTextEmail.text.toString()
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            val qualifications = binding.editTextQualifications.text.toString()
            val subjectsTaught = binding.editTextSubjectsTaught.text.toString()
            val hourlyRate = binding.editTextHourlyRate.text.toString()
            val biography = binding.editTextBiography.text.toString()

            // Validate the inputs
            if (firstName.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty() || phoneNumber.isEmpty() || qualifications.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create Lecturer object
            val lecturer = Lecturer(
                role = "lecturer",
                firstName = firstName,
                lastName = lastName,
                dateOfBirth = dateOfBirth,
                email = email,
                phoneNumber = phoneNumber,
                qualifications = qualifications,
                subjectsTaught = subjectsTaught,
                hourlyRate = hourlyRate,
                biography = biography
            )

            // Save the lecturer data to Firestore under the user's UID
            val userRef = firestore.collection("Lecturer").document(userId)
            userRef.set(lecturer)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()

                    // Redirect to LoginActivity after saving the profile
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save profile: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
