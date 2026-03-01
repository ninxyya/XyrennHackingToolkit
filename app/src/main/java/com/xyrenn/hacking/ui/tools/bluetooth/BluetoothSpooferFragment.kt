package com.xyrenn.hacking.ui.tools.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentBluetoothSpooferBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BluetoothSpooferFragment : Fragment() {

    private var _binding: FragmentBluetoothSpooferBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BluetoothSpooferViewModel by viewModels()
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            viewModel.checkBluetoothState()
        } else {
            Toast.makeText(requireContext(), "Bluetooth permissions required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBluetoothSpooferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        setupSpinners()
        setupClickListeners()
        observeViewModel()
        checkPermissions()
    }

    private fun setupSpinners() {
        val deviceTypes = arrayOf("Headphones", "Speaker", "Phone", "Watch", "Keyboard", "Mouse")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, deviceTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDeviceType.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnGenerateSpoof.setOnClickListener {
            val deviceName = binding.etDeviceName.text.toString()
            val deviceType = binding.spinnerDeviceType.selectedItem.toString()

            if (deviceName.isNotEmpty()) {
                viewModel.generateSpoofedDevice(deviceName, deviceType)
            } else {
                Toast.makeText(requireContext(), "Enter device name", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStartAdvertising.setOnClickListener {
            viewModel.startAdvertising()
        }

        binding.btnStopAdvertising.setOnClickListener {
            viewModel.stopAdvertising()
        }
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADVERTISE)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            }
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }

        if (permissions.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissions.toTypedArray())
        } else {
            viewModel.checkBluetoothState()
        }
    }

    private fun observeViewModel() {
        viewModel.spoofedDevice.observe(viewLifecycleOwner) { device ->
            binding.tvSpoofedDevice.text = "Spoofed: ${device.name} (${device.type})"
        }

        viewModel.isAdvertising.observe(viewLifecycleOwner) { advertising ->
            binding.btnStartAdvertising.isEnabled = !advertising
            binding.btnStopAdvertising.isEnabled = advertising
            binding.progressBar.visibility = if (advertising) View.VISIBLE else View.GONE
            binding.tvStatus.text = if (advertising) "Advertising active" else "Advertising stopped"
        }

        viewModel.isBluetoothEnabled.observe(viewLifecycleOwner) { enabled ->
            if (!enabled) {
                binding.tvStatus.text = "Please enable Bluetooth"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}