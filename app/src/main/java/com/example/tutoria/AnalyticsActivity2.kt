package com.example.tutoria

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class AnalyticsActivity2 : AppCompatActivity() {

    private lateinit var activeUsersTextView: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        activeUsersTextView = findViewById(R.id.activeUsersTextView)
        database = FirebaseDatabase.getInstance().reference

        fetchAnalyticsData()
    }

    private fun fetchAnalyticsData() {
        database.child("analytics").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val activeUsers = snapshot.child("activeUsers").getValue(Int::class.java) ?: 0
                activeUsersTextView.text = "Active Users: $activeUsers"
            }

            override fun onCancelled(error: DatabaseError) {
                activeUsersTextView.text = "Error loading data"
            }
        })
    }
}
