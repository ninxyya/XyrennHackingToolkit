package com.xyrenn.hacking.ui.tools.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentPacketGeneratorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PacketGeneratorFragment : Fragment() {

    private var _binding: FragmentPacketGeneratorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PacketGeneratorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPacketGeneratorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnGenerateUdp.setOnClickListener {
            val targetIp = binding.etTargetIp.text.toString()
            val port = binding.etPort.text.toString().toIntOrNull() ?: 12345
            val data = binding.etData.text.toString()
            val count = binding.etCount.text.toString().toIntOrNull() ?: 1

            if (targetIp.isNotEmpty()) {
                viewModel.generateUdpPackets(targetIp, port, data, count)
            } else {
                Toast.makeText(requireContext(), "Enter target IP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGenerateTcp.setOnClickListener {
            val targetIp = binding.etTargetIp.text.toString()
            val port = binding.etPort.text.toString().toIntOrNull() ?: 12345
            val data = binding.etData.text.toString()
            val count = binding.etCount.text.toString().toIntOrNull() ?: 1

            if (targetIp.isNotEmpty()) {
                viewModel.generateTcpPackets(targetIp, port, data, count)
            } else {
                Toast.makeText(requireContext(), "Enter target IP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStop.setOnClickListener {
            viewModel.stopGeneration()
        }
    }

    private fun observeViewModel() {
        viewModel.isGenerating.observe(viewLifecycleOwner) { generating ->
            binding.btnGenerateUdp.isEnabled = !generating
            binding.btnGenerateTcp.isEnabled = !generating
            binding.btnStop.isEnabled = generating
            binding.progressBar.visibility = if (generating) View.VISIBLE else View.GONE
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
        }

        viewModel.packetsGenerated.observe(viewLifecycleOwner) { packets ->
            binding.tvPacketsGenerated.text = "Packets: $packets"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}