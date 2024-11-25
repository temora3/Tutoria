package com.example.tutoria.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutoria.data.User
import com.example.tutoria.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID

class UserAccountViewModel(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: StorageReference,
    app: Application
) : AndroidViewModel(app) {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateInfo = _updateInfo.asStateFlow()

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            _user.emit(Resource.Loading())
            try {
                val userDoc = fireStore.collection("Users")
                    .document(auth.uid ?: throw IllegalStateException("User not authenticated"))
                    .get()
                    .await()

                val user = userDoc.toObject(User::class.java)
                _user.emit(user?.let { Resource.Success(it) } ?: Resource.Error("User data not found"))
            } catch (e: Exception) {
                _user.emit(Resource.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }

    fun updateUser(user: User, imageUri: Uri?) {
        viewModelScope.launch {
            _updateInfo.emit(Resource.Loading())
            try {
                val updatedUser = if (imageUri != null) {
                    val imageUrl = uploadUserImage(imageUri)
                    user.copy(imagePath = imageUrl)
                } else user

                saveUserInformation(updatedUser)
                _updateInfo.emit(Resource.Success(updatedUser))
            } catch (e: Exception) {
                _updateInfo.emit(Resource.Error(e.message ?: "Failed to update user"))
            }
        }
    }

    private suspend fun uploadUserImage(imageUri: Uri): String {
        val bitmap = loadImageBitmap(imageUri)
        val byteArray = compressImage(bitmap)
        val imageDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
        return imageDirectory.putBytes(byteArray).await()
            .storage.downloadUrl.await().toString()
    }

    private fun loadImageBitmap(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(getApplication<Application>().contentResolver, imageUri)
            )
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(
                getApplication<Application>().contentResolver, imageUri
            )
        }
    }

    private fun compressImage(bitmap: Bitmap): ByteArray {
        return ByteArrayOutputStream().apply {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, this)
        }.toByteArray()
    }

    private suspend fun saveUserInformation(user: User) {
        fireStore.collection("Users")
            .document(auth.uid!!)
            .set(user)
            .await()
    }
}
