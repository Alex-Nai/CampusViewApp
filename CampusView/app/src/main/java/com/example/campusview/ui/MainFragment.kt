package com.example.campusview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campusview.R
import com.example.campusview.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding.classroomCard.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_classroom)
        }

        binding.bookingCard.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_booking)
        }

        binding.spotsCard.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_spots)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 