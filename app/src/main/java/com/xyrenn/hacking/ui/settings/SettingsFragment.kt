package com.xyrenn.hacking.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentSettingsBinding
import com.xyrenn.hacking.ui.settings.adapters.SettingsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        settingsAdapter = SettingsAdapter { setting ->
            when (setting.id) {
                "permissions" -> navigateToPermissionManager()
                "theme" -> navigateToTheme()
                "notifications" -> showNotificationSettings()
                "about" -> navigateToAbout()
                "root" -> viewModel.toggleRootAccess()
                "stealth" -> viewModel.toggleStealthMode()
                "auto_start" -> viewModel.toggleAutoStart()
                "save_logs" -> viewModel.toggleSaveLogs()
                "clear_data" -> viewModel.clearAppData()
                "export_logs" -> viewModel.exportLogs()
            }
        }
        binding.rvSettings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSettings.adapter = settingsAdapter
    }

    private fun navigateToPermissionManager() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.settings_container, PermissionManagerFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToTheme() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.settings_container, ThemeFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToAbout() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.settings_container, AboutFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showNotificationSettings() {
        // Show notification settings dialog
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Notification Settings")
            .setMessage("Configure notification preferences")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.settings.observe(viewLifecycleOwner) { settings ->
            settingsAdapter.submitList(settings)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}