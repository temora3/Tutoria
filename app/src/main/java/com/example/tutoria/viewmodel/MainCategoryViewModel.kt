package com.example.tutoria.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tutoria.resource.Resource
import com.example.tutoria.data.SpecialTutor
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainCategoryViewModel(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialTutors = MutableStateFlow<Resource<List<SpecialTutor>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<SpecialTutor>>> = _specialTutors

    init {
        fetchSpecialTutors()
    }

    fun fetchSpecialTutors() {
        firestore.collection("TutorDetails")
            .whereEqualTo("category", "Languages")
            .get()
            .addOnSuccessListener { result ->
                val specialTutorList = result.toObjects(SpecialTutor::class.java)
                viewModelScope.launch {
                    _specialTutors.emit(Resource.Success(specialTutorList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialTutors.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}

class MainCategoryViewModelFactory(
    private val firestore: FirebaseFirestore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainCategoryViewModel(firestore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
