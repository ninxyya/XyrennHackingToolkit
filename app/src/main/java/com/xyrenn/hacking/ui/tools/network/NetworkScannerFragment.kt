package com.xyrenn.hacking.ui.tools.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentNetworkScannerBinding
import com.xyrenn.hacking.ui.tools.network.adapters.DeviceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetworkScannerFragment : Fragment() {

    private var _binding: FragmentNetworkScannerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NetworkScannerViewModel by viewModels()
    private lateinit var adapter: DeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNetworkScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = DeviceAdapter()
        binding.rvDevices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDevices.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnStartScan.setOnClickListener {
            val subnet = binding.etSubnet.text.toString()
            if (subnet.isNotEmpty()) {
                viewModel.scanNetwork(subnet)
            } else {
                Toast.makeText(requireContext(), "Enter subnet (e.g., 192.168.1.0/24)", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStopScan.setOnClickListener {
            viewModel.stopScan()
        }
    }

    private fun observeViewModel() {
        viewModel.devices.observe(viewLifecycleOwner) { devices ->
            adapter.submitList(devices)
            binding.tvDeviceCount.text = "Devices: ${devices.size}"
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