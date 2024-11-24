package com.example.tutoria

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tutoria.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.tutoria.mvm.User

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()
            val selectedRoleId = binding.roleRadioGroup.checkedRadioButtonId

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (selectedRoleId == -1) {
                    Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val role = findViewById<RadioButton>(selectedRoleId)?.text.toString()

                if (password == confirmPassword) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        // Disable signup button while processing
                        binding.signupButton.isEnabled = false

                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener { authResult ->
                                val userId = authResult.user?.uid
                                if (userId != null) {
                                    val userRef = firebaseDatabase.reference
                                        .child("Users")
                                        .child(userId)

                                    // Create user object with profileCompleted = false
                                    val user = User(email, role, profileCompleted = false)

                                    userRef.setValue(user)
                                        .addOnSuccessListener {
                                            Log.d("SignupActivity", "User data saved successfully")
                                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                                            // Redirect based on role
                                            val intent = if (role == "Student") {
                                                Intent(this, StudentSetUpProfileActivity::class.java)
                                            } else {
                                                Intent(this, LecturerSetUpProfileActivity::class.java)
                                            }

                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            intent.putExtra("USER_ID", userId)
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            binding.signupButton.isEnabled = true
                                            Log.e("SignupActivity", "Failed to save user data", e)
                                            Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                binding.signupButton.isEnabled = true
                                Log.e("SignupActivity", "Authentication failed", e)
                                Toast.makeText(this, "Authentication failed: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
