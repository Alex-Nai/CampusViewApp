package com.example.campusview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.campusview.R
import com.example.campusview.databinding.FragmentLoginBinding
import com.example.campusview.viewmodel.MainViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "请输入用户名和密码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            viewModel.login(username, password)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess {
                findNavController().navigate(R.id.action_login_to_main)
            }.onFailure {
                Toast.makeText(requireContext(), "登录失败: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 