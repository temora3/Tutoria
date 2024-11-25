package com.example.tutoria.fragments.tutoring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tutoria.R
import com.example.tutoria.adapters.HomePageViewAdapter
import com.example.tutoria.databinding.FragmentHomeBinding
import com.example.tutoria.fragments.tutoring.categories.HumanitiesFragment
import com.example.tutoria.fragments.tutoring.categories.LanguagesFragment
import com.example.tutoria.fragments.tutoring.categories.MainCategoryFragment
import com.example.tutoria.fragments.tutoring.categories.MathFragment
import com.example.tutoria.fragments.tutoring.categories.StandardUnitFragement
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf(
            MainCategoryFragment(),
            MathFragment(),
            LanguagesFragment(),
            HumanitiesFragment(),
            StandardUnitFragement()
        )

        val viewPager2Adapter =
            HomePageViewAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, position ->
            tab.text = when (position) {
                0 -> "Main"
                1 -> "Math"
                2 -> "Languages"
                3 -> "Humanities"
                else -> "Standard Units"
            }
        }.attach()
    }
}
