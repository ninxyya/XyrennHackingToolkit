package com.xyrenn.hacking.ui.tools.crypto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentDecryptorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DecryptorFragment : Fragment() {

    private var _binding: FragmentDecryptorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DecryptorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDecryptorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnDecrypt.setOnClickListener {
            val encrypted = binding.etInput.text.toString()
            val password = binding.etPassword.text.toString()

            if (encrypted.isNotEmpty() && password.isNotEmpty()) {
                viewModel.decryptText(encrypted, password)
            } else {
                Toast.makeText(requireContext(), "Enter encrypted text and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPaste.setOnClickListener {
            viewModel.pasteFromClipboard()
        }

        binding.btnClear.setOnClickListener {
            viewModel.clear()
        }
    }

    private fun observeViewModel() {
        viewModel.decryptedText.observe(viewLifecycleOwner) { decrypted ->
            binding.tvOutput.text = decrypted
        }

        viewModel.isProcessing.observe(viewLifecycleOwner) { processing ->
            binding.btnDecrypt.isEnabled = !processing
            binding.progressBar.visibility = if (processing) View.VISIBLE else View.GONE
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}