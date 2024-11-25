package com.example.tutoria.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

class UserAccountViewModelFactory(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: StorageReference,
    private val app: Application
) : ViewModelProvider.AndroidViewModelFactory(app) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserAccountViewModel::class.java)) {
            return UserAccountViewModel(fireStore, auth, storage, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
