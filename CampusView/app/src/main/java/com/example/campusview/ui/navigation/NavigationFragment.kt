package com.example.campusview.ui.navigation

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.example.campusview.databinding.FragmentNavigationBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class NavigationFragment : Fragment() {

    private var _binding: FragmentNavigationBinding? = null
    private val binding get() = _binding!!
    private var aMap: AMap? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                // TODO: 实现图像识别获取目的地
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNavigationBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        setupButtons()
    }

    private fun setupMap() {
        aMap = binding.mapView.map
        aMap?.apply {
            // 设置地图初始位置（这里需要设置为学校的经纬度）
            val schoolLocation = LatLng(39.908823, 116.397470)
            moveCamera(CameraUpdateFactory.newLatLngZoom(schoolLocation, 17f))
        }
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

        binding.buttonStartNavigation.setOnClickListener {
            val destination = binding.destinationInput.text.toString()
            if (destination.isNotEmpty()) {
                // TODO: 实现导航功能
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
} 