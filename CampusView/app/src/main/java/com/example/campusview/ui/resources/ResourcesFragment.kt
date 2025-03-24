package com.example.campusview.ui.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.campusview.databinding.FragmentResourcesBinding
import com.google.android.material.tabs.TabLayoutMediator

class ResourcesFragment : Fragment() {

    private var _binding: FragmentResourcesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupFab()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ClassroomFragment()
                    1 -> BookableResourceFragment()
                    else -> throw IllegalArgumentException("Invalid position $position")
                }
            }
        }

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "教室资源"
                1 -> "可预约资源"
                else -> throw IllegalArgumentException("Invalid position $position")
            }
        }.attach()
    }

    private fun setupFab() {
        binding.fabBook.setOnClickListener {
            // TODO: 实现预约功能
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 