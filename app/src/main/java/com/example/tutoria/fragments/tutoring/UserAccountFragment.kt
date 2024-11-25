package com.example.tutoria.fragments.tutoring

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import android.app.Activity.RESULT_OK
import com.bumptech.glide.Glide
import com.example.tutoria.R
import com.example.tutoria.data.User
import com.example.tutoria.databinding.FragmentUserAccountBinding
import com.example.tutoria.resource.Resource
import com.example.tutoria.viewmodel.UserAccountViewModel
import com.example.tutoria.viewmodel.UserAccountViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserAccountFragment : Fragment() {
    private lateinit var binding: FragmentUserAccountBinding

    // Use the manual ViewModel initialization
    private lateinit var viewModel: UserAccountViewModel

    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private val animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.button_loading) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ViewModel with a factory
        val factory = UserAccountViewModelFactory(
            fireStore = FirebaseFirestore.getInstance(),
            auth = FirebaseAuth.getInstance(),
            storage = FirebaseStorage.getInstance().reference,
            app = requireActivity().application
        )
        viewModel = ViewModelProvider(this, factory)[UserAccountViewModel::class.java]

        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    imageUri = result.data?.data
                    Glide.with(this).load(imageUri).into(binding.imageUser)
                } else {
                    Toast.makeText(requireContext(), "Image selection canceled", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.user.collectLatest { resource ->
                        when (resource) {
                            is Resource.Loading -> showUserLoading()
                            is Resource.Success -> {
                                hideUserLoading()
                                resource.data?.let { showUserInformation(it) }
                            }
                            is Resource.Error -> {
                                hideUserLoading()
                                Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                            }
                            else -> Unit
                        }
                    }
                }

                launch {
                    viewModel.updateInfo.collectLatest { resource ->
                        when (resource) {
                            is Resource.Loading -> binding.buttonSave.startAnimation(animation)
                            is Resource.Success -> {
                                binding.buttonSave.clearAnimation()
                                findNavController().navigateUp()
                            }
                            is Resource.Error -> {
                                binding.buttonSave.clearAnimation()
                                Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
            imageActivityResultLauncher.launch(intent)
        }

        binding.buttonSave.setOnClickListener {
            val firstName = binding.edFirstName.text.toString().trim()
            val lastName = binding.edLastName.text.toString().trim()
            val email = binding.edEmail.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(firstName = firstName, lastName = lastName, email = email)
            viewModel.updateUser(user, imageUri)
        }
    }

    private fun showUserInformation(user: User) {
        binding.apply {
            Glide.with(this@UserAccountFragment).load(user.imagePath)
                .error(ColorDrawable(Color.GRAY))
                .into(imageUser)
            edFirstName.setText(user.firstName)
            edLastName.setText(user.lastName)
            edEmail.setText(user.email)
        }
    }

    private fun hideUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.GONE
            imageUser.visibility = View.VISIBLE
            imageEdit.visibility = View.VISIBLE
            edFirstName.visibility = View.VISIBLE
            edLastName.visibility = View.VISIBLE
            edEmail.visibility = View.VISIBLE
            tvUpdatePassword.visibility = View.VISIBLE
        }
    }

    private fun showUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.VISIBLE
            imageUser.visibility = View.INVISIBLE
            imageEdit.visibility = View.INVISIBLE
            edFirstName.visibility = View.INVISIBLE
            edLastName.visibility = View.INVISIBLE
            edEmail.visibility = View.INVISIBLE
            tvUpdatePassword.visibility = View.INVISIBLE
        }
    }
}
