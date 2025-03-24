package com.example.campusview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusview.databinding.FragmentClassroomBinding
import com.example.campusview.ui.adapter.ClassroomAdapter
import com.example.campusview.ui.viewmodel.ClassroomViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassroomFragment : Fragment() {
    private var _binding: FragmentClassroomBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ClassroomViewModel by viewModels()
    private val adapter = ClassroomAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
        loadClassrooms()
    }

    private fun setupViews() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ClassroomFragment.adapter
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.loadAllClassrooms()
                    1 -> viewModel.loadClassroomsByBuilding("主楼")
                    2 -> viewModel.loadClassroomsByBuilding("中楼")
                    3 -> viewModel.loadAvailableClassrooms()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.filterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.allChip.id -> viewModel.loadAllClassrooms()
                binding.availableChip.id -> viewModel.loadAvailableClassrooms()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.classrooms.observe(viewLifecycleOwner) { classrooms ->
            adapter.submitList(classrooms)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun loadClassrooms() {
        viewModel.loadAllClassrooms()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 