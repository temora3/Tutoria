package com.example.tutoria.fragments.tutoring.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutoria.R
import com.example.tutoria.adapters.SpecialTutorAdapter
import com.example.tutoria.databinding.FragmentMainCategoryBinding
import com.example.tutoria.resource.Resource
import com.example.tutoria.viewmodel.MainCategoryViewModel
import com.example.tutoria.viewmodel.MainCategoryViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainCategoryFragment"

class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialTutorAdapter: SpecialTutorAdapter
    private lateinit var viewModel: MainCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel manually
        val firestore = FirebaseFirestore.getInstance()
        val factory = MainCategoryViewModelFactory(firestore)
        viewModel = ViewModelProvider(this, factory)[MainCategoryViewModel::class.java]

        setupSpecialProductsRv()

        // Collect data from the ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.specialProducts.collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            showLoading()
                        }
                        is Resource.Success -> {
                            specialTutorAdapter.differ.submitList(resource.data)
                            hideLoading()
                        }
                        is Resource.Error -> {
                            hideLoading()
                            Log.e(TAG, resource.message.toString())
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Unspecified -> {
                            // Handle the unspecified case or remove if unnecessary
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductsRv() {
        specialTutorAdapter = SpecialTutorAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialTutorAdapter
        }
    }
}
