package com.xyrenn.hacking.ui.tools.advanced

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentCustomPayloadBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomPayloadFragment : Fragment() {

    private var _binding: FragmentCustomPayloadBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CustomPayloadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomPayloadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinners()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupSpinners() {
        val payloadTypes = arrayOf("Reverse Shell", "Bind Shell", "Meterpreter", "Custom")
        val osTypes = arrayOf("Android", "Windows", "Linux", "macOS", "iOS")
        val formats = arrayOf("APK", "EXE", "ELF", "Mach-O", "Python", "PHP", "JSP")

        val typeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, payloadTypes)
        val osAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, osTypes)
        val formatAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, formats)

        binding.spinnerPayloadType.adapter = typeAdapter
        binding.spinnerTargetOs.adapter = osAdapter
        binding.spinnerOutputFormat.adapter = formatAdapter
    }

    private fun setupClickListeners() {
        binding.btnGeneratePayload.setOnClickListener {
            val lhost = binding.etLhost.text.toString()
            val lport = binding.etLport.text.toString()
            val payloadType = binding.spinnerPayloadType.selectedItem.toString()
            val targetOs = binding.spinnerTargetOs.selectedItem.toString()
            val format = binding.spinnerOutputFormat.selectedItem.toString()

            if (lhost.isNotEmpty() && lport.isNotEmpty()) {
                viewModel.generatePayload(lhost, lport, payloadType, targetOs, format)
            } else {
                Toast.makeText(requireContext(), "Enter LHOST and LPORT", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSavePayload.setOnClickListener {
            viewModel.savePayload()
        }
    }

    private fun observeViewModel() {
        viewModel.isGenerating.observe(viewLifecycleOwner) { generating ->
            binding.btnGeneratePayload.isEnabled = !generating
            binding.progressBar.visibility = if (generating) View.VISIBLE else View.GONE
        }

        viewModel.generatedPayload.observe(viewLifecycleOwner) { payload ->
            binding.tvPayloadOutput.text = payload
            binding.btnSavePayload.isEnabled = payload.isNotEmpty()
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