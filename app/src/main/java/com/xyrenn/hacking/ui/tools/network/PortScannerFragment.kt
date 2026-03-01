package com.xyrenn.hacking.ui.tools.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentPortScannerBinding
import com.xyrenn.hacking.ui.tools.network.adapters.PortAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortScannerFragment : Fragment() {

    private var _binding: FragmentPortScannerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PortScannerViewModel by viewModels()
    private lateinit var adapter: PortAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = PortAdapter()
        binding.rvPorts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPorts.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnStartScan.setOnClickListener {
            val targetIp = binding.etTargetIp.text.toString()
            val startPort = binding.etStartPort.text.toString().toIntOrNull() ?: 1
            val endPort = binding.etEndPort.text.toString().toIntOrNull() ?: 1024

            if (targetIp.isNotEmpty()) {
                viewModel.scanPorts(targetIp, startPort, endPort)
            } else {
                Toast.makeText(requireContext(), "Enter target IP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStopScan.setOnClickListener {
            viewModel.stopScan()
        }
    }

    private fun observeViewModel() {
        viewModel.ports.observe(viewLifecycleOwner) { ports ->
            adapter.submitList(ports)
            binding.tvPortCount.text = "Open ports: ${ports.size}"
        }

        viewModel.isScanning.observe(viewLifecycleOwner) { scanning ->
            binding.btnStartScan.isEnabled = !scanning
            binding.btnStopScan.isEnabled = scanning
            binding.progressBar.visibility = if (scanning) View.VISIBLE else View.GONE
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