package com.xyrenn.hacking.ui.tools.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentBluetoothScannerBinding
import com.xyrenn.hacking.ui.tools.bluetooth.adapters.BluetoothDeviceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BluetoothScannerFragment : Fragment() {

    private var _binding: FragmentBluetoothScannerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BluetoothScannerViewModel by viewModels()
    private lateinit var deviceAdapter: BluetoothDeviceAdapter
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.checkBluetoothState()
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            viewModel.startScan()
        } else {
            Toast.makeText(requireContext(), "Bluetooth permissions required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBluetoothScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bluetoothManager = requireContext().getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        viewModel.checkBluetoothState()
    }

    private fun setupRecyclerView() {
        deviceAdapter = BluetoothDeviceAdapter { device ->
            viewModel.selectDevice(device)
        }
        binding.rvDevices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDevices.adapter = deviceAdapter
    }

    private fun setupClickListeners() {
        binding.btnEnableBluetooth.setOnClickListener {
            enableBluetooth()
        }

        binding.btnStartScan.setOnClickListener {
            checkPermissionsAndScan()
        }

        binding.btnStopScan.setOnClickListener {
            viewModel.stopScan()
        }
    }

    private fun enableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(intent)
        }
    }

    private fun checkPermissionsAndScan() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        if (permissions.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissions.toTypedArray())
        } else {
            viewModel.startScan()
        }
    }

    private fun observeViewModel() {
        viewModel.devices.observe(viewLifecycleOwner) { devices ->
            deviceAdapter.submitList(devices)
            binding.tvDeviceCount.text = "Devices: ${devices.size}"
        }

        viewModel.isScanning.observe(viewLifecycleOwner) { scanning ->
            binding.btnStartScan.isEnabled = !scanning
            binding.btnStopScan.isEnabled = scanning
            binding.progressBar.visibility = if (scanning) View.VISIBLE else View.GONE
            binding.tvStatus.text = if (scanning) "Scanning..." else "Ready"
        }

        viewModel.isBluetoothEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.btnEnableBluetooth.visibility = if (enabled) View.GONE else View.VISIBLE
            binding.btnStartScan.isEnabled = enabled
            binding.tvStatus.text = if (enabled) "Bluetooth enabled" else "Bluetooth disabled"
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