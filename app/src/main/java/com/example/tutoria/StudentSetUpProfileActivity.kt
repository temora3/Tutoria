package com.example.tutoria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tutoria.mvm.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentSetUpProfileActivity : AppCompatActivity() {

    private lateinit var firstNameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var dateOfBirthInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneNumberInput: EditText
    private lateinit var subjectsOfInterestInput: EditText
    private lateinit var learningGoalsInput: EditText
    private lateinit var saveProfileButton: Button

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_student_profile)  // Correct layout reference

        // Initialize Firebase Authentication and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Bind the UI elements
        firstNameInput = findViewById(R.id.editTextFirstName)
        lastNameInput = findViewById(R.id.editTextLastName)
        dateOfBirthInput = findViewById(R.id.editTextDateOfBirth)
        emailInput = findViewById(R.id.editTextEmail)
        phoneNumberInput = findViewById(R.id.editTextPhoneNumber)
        subjectsOfInterestInput = findViewById(R.id.editTextSubjectsOfInterest)
        learningGoalsInput = findViewById(R.id.editTextLearningGoals)
        saveProfileButton = findViewById(R.id.buttonSaveProfile)

        // Set up the save profile button
        saveProfileButton.setOnClickListener {
            saveStudentProfile()
        }
    }

    private fun saveStudentProfile() {
        val firstName = firstNameInput.text.toString()
        val lastName = lastNameInput.text.toString()
        val dateOfBirth = dateOfBirthInput.text.toString()
        val email = emailInput.text.toString()
        val phoneNumber = phoneNumberInput.text.toString()
        val subjectsOfInterest = subjectsOfInterestInput.text.toString()
        val learningGoals = learningGoalsInput.text.toString()

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() ||
            subjectsOfInterest.isEmpty() || learningGoals.isEmpty()) {
            // Show error message
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create Student object
        val student = Student(
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            email = email,
            phoneNumber = phoneNumber,
            subjectsOfInterest = subjectsOfInterest,
            learningGoals = learningGoals
        )

        // Save student data to Firestore under the user's UID
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            firestore.collection("Students") // This will create the collection if it doesn't exist
                .document(userId) // Use the userId as the document ID
                .set(student) // Save the student object
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Successfully saved the student profile
                        Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show()

                        // Redirect to LoginActivity after successful profile creation
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(loginIntent)
                        finish() // Close the current activity
                    } else {
                        // Handle failure (show an error message)
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "User is not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
}
