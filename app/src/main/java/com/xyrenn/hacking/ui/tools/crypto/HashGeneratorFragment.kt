package com.xyrenn.hacking.ui.tools.crypto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentHashGeneratorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HashGeneratorFragment : Fragment() {

    private var _binding: FragmentHashGeneratorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HashGeneratorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHashGeneratorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupSpinner() {
        val hashTypes = arrayOf("MD5", "SHA-1", "SHA-256", "SHA-512", "CRC32")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hashTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerHashType.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnGenerate.setOnClickListener {
            val text = binding.etInput.text.toString()
            val hashType = binding.spinnerHashType.selectedItem.toString()

            if (text.isNotEmpty()) {
                viewModel.generateHash(text, hashType)
            } else {
                Toast.makeText(requireContext(), "Enter text", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCopy.setOnClickListener {
            val hash = binding.tvOutput.text.toString()
            if (hash.isNotEmpty() && hash != "Hash will appear here") {
                viewModel.copyToClipboard(hash)
            }
        }

        binding.btnCompare.setOnClickListener {
            val hash1 = binding.etHash1.text.toString()
            val hash2 = binding.etHash2.text.toString()

            if (hash1.isNotEmpty() && hash2.isNotEmpty()) {
                viewModel.compareHashes(hash1, hash2)
            } else {
                Toast.makeText(requireContext(), "Enter both hashes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.generatedHash.observe(viewLifecycleOwner) { hash ->
            binding.tvOutput.text = hash
        }

        viewModel.isProcessing.observe(viewLifecycleOwner) { processing ->
            binding.btnGenerate.isEnabled = !processing
            binding.progressBar.visibility = if (processing) View.VISIBLE else View.GONE
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
        }

        viewModel.compareResult.observe(viewLifecycleOwner) { result ->
            binding.tvCompareResult.text = result
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}