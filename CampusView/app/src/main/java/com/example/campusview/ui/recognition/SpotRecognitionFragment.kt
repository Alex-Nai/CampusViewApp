package com.example.campusview.ui.recognition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.campusview.databinding.FragmentSpotRecognitionBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class SpotRecognitionFragment : Fragment() {

    private var _binding: FragmentSpotRecognitionBinding? = null
    private val binding get() = _binding!!

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                binding.imageView.setImageURI(it)
                // TODO: 实现图像识别逻辑
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpotRecognitionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    private fun setupButtons() {
        binding.buttonTakePhoto.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }

        binding.buttonPickImage.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 