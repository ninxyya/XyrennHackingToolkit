package com.xyrenn.hacking.ui.tools.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentDeviceDiscoveryBinding
import com.xyrenn.hacking.ui.tools.remote.adapters.DiscoveredDeviceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceDiscoveryFragment : Fragment() {

    private var _binding: FragmentDeviceDiscoveryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DeviceDiscoveryViewModel by viewModels()
    private lateinit var adapter: DiscoveredDeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = DiscoveredDeviceAdapter { device ->
            viewModel.selectDevice(device)
        }
        binding.rvDevices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDevices.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnStartDiscovery.setOnClickListener {
            viewModel.startDiscovery()
        }

        binding.btnStopDiscovery.setOnClickListener {
            viewModel.stopDiscovery()
        }
    }

    private fun observeViewModel() {
        viewModel.devices.observe(viewLifecycleOwner) { devices ->
            adapter.submitList(devices)
            binding.tvDeviceCount.text = "Devices: ${devices.size}"
        }

        viewModel.isDiscovering.observe(viewLifecycleOwner) { discovering ->
            binding.btnStartDiscovery.isEnabled = !discovering
            binding.btnStopDiscovery.isEnabled = discovering
            binding.progressBar.visibility = if (discovering) View.VISIBLE else View.GONE
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