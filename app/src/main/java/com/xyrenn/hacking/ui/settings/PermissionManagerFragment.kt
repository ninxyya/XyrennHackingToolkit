package com.xyrenn.hacking.ui.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentPermissionManagerBinding
import com.xyrenn.hacking.ui.settings.adapters.PermissionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionManagerFragment : Fragment() {

    private var _binding: FragmentPermissionManagerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PermissionManagerViewModel by viewModels()
    private lateinit var permissionAdapter: PermissionAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
        viewModel.loadPermissions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        permissionAdapter = PermissionAdapter { permission ->
            handlePermissionClick(permission)
        }
        binding.rvPermissions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPermissions.adapter = permissionAdapter
    }

    private fun setupClickListeners() {
        binding.btnRefresh.setOnClickListener {
            viewModel.loadPermissions()
        }
    }

    private fun handlePermissionClick(permission: com.xyrenn.hacking.ui.settings.models.Permission) {
        when {
            permission.isGranted -> {
                // Permission already granted, show info
                Toast.makeText(requireContext(), "${permission.name} is already granted", Toast.LENGTH_SHORT).show()
            }
            permission.shouldShowRationale -> {
                // Show rationale dialog
                showPermissionRationale(permission)
            }
            else -> {
                // Request permission
                requestPermissionLauncher.launch(permission.androidPermission)
            }
        }
    }

    private fun showPermissionRationale(permission: com.xyrenn.hacking.ui.settings.models.Permission) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage(permission.rationale)
            .setPositiveButton("Grant") { _, _ ->
                requestPermissionLauncher.launch(permission.androidPermission)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.permissions.observe(viewLifecycleOwner) { permissions ->
            permissionAdapter.submitList(permissions)
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