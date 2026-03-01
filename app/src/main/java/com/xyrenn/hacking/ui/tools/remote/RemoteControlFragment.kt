package com.xyrenn.hacking.ui.tools.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentRemoteControlBinding
import com.xyrenn.hacking.ui.tools.remote.adapters.RemoteDeviceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteControlFragment : Fragment() {

    private var _binding: FragmentRemoteControlBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RemoteControlViewModel by viewModels()
    private lateinit var deviceAdapter: RemoteDeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRemoteControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        deviceAdapter = RemoteDeviceAdapter { device ->
            viewModel.selectDevice(device)
        }
        binding.rvDevices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDevices.adapter = deviceAdapter
    }

    private fun setupClickListeners() {
        binding.btnDiscover.setOnClickListener {
            viewModel.discoverDevices()
        }

        binding.btnConnect.setOnClickListener {
            viewModel.connectToSelectedDevice()
        }

        binding.btnDisconnect.setOnClickListener {
            viewModel.disconnectDevice()
        }

        binding.btnSendCommand.setOnClickListener {
            val command = binding.etCommand.text.toString()
            if (command.isNotEmpty()) {
                viewModel.sendCommand(command)
                binding.etCommand.text.clear()
            } else {
                Toast.makeText(requireContext(), "Enter command", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.devices.observe(viewLifecycleOwner) { devices ->
            deviceAdapter.submitList(devices)
            binding.tvDeviceCount.text = "Devices: ${devices.size}"
        }

        viewModel.isDiscovering.observe(viewLifecycleOwner) { discovering ->
            binding.btnDiscover.isEnabled = !discovering
            binding.progressBar.visibility = if (discovering) View.VISIBLE else View.GONE
        }

        viewModel.connectionStatus.observe(viewLifecycleOwner) { status ->
            binding.tvConnectionStatus.text = status
            binding.btnConnect.isEnabled = viewModel.selectedDevice.value != null && !viewModel.isConnected.value!!
            binding.btnDisconnect.isEnabled = viewModel.isConnected.value == true
            binding.etCommand.isEnabled = viewModel.isConnected.value == true
            binding.btnSendCommand.isEnabled = viewModel.isConnected.value == true
        }

        viewModel.commandOutput.observe(viewLifecycleOwner) { output ->
            binding.tvCommandOutput.append("$output\n")
            // Auto scroll to bottom
            binding.scrollView.post {
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}