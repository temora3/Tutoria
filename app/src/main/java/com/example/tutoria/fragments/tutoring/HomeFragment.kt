package com.example.tutoria.fragments.tutoring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.tutoria.R
import com.example.tutoria.adapters.HomePageViewAdapter
import com.example.tutoria.databinding.FragmentHomeBinding
import com.example.tutoria.fragments.tutoring.categories.HumanitiesFragment
import com.example.tutoria.fragments.tutoring.categories.LanguagesFragment
import com.example.tutoria.fragments.tutoring.categories.MainCategoryFragment
import com.example.tutoria.fragments.tutoring.categories.MathFragment
import com.example.tutoria.fragments.tutoring.categories.StandardUnitFragement

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstancestate: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            MathFragment(),
            LanguagesFragment(),
            HumanitiesFragment(),
            StandardUnitFragement()
        )

        val viewPager2Adapter = HomePageViewAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter = viewPager2Adapter
    }
}
