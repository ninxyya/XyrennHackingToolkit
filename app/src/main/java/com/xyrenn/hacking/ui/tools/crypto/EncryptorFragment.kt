package com.xyrenn.hacking.ui.tools.crypto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentEncryptorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EncryptorFragment : Fragment() {

    private var _binding: FragmentEncryptorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EncryptorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEncryptorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnEncrypt.setOnClickListener {
            val text = binding.etInput.text.toString()
            val password = binding.etPassword.text.toString()

            if (text.isNotEmpty() && password.isNotEmpty()) {
                viewModel.encryptText(text, password)
            } else {
                Toast.makeText(requireContext(), "Enter text and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCopy.setOnClickListener {
            val encrypted = binding.tvOutput.text.toString()
            if (encrypted.isNotEmpty() && encrypted != "Encrypted text will appear here") {
                viewModel.copyToClipboard(encrypted)
            }
        }

        binding.btnClear.setOnClickListener {
            viewModel.clear()
        }
    }

    private fun observeViewModel() {
        viewModel.encryptedText.observe(viewLifecycleOwner) { encrypted ->
            binding.tvOutput.text = encrypted
        }

        viewModel.isProcessing.observe(viewLifecycleOwner) { processing ->
            binding.btnEncrypt.isEnabled = !processing
            binding.progressBar.visibility = if (processing) View.VISIBLE else View.GONE
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
            if (status.contains("Copied")) {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}