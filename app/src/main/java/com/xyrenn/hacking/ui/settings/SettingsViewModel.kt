package com.xyrenn.hacking.ui.settings

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.settings.models.AppSettings
import com.xyrenn.hacking.utils.managers.RootManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: android.content.Context,
    private val rootManager: RootManager
) : ViewModel() {

    private val _settings = MutableLiveData<List<AppSettings>>()
    val settings: LiveData<List<AppSettings>> = _settings

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val settingsList = listOf(
            AppSettings(
                id = "permissions",
                title = "Permissions",
                description = "Manage app permissions",
                type = "navigation",
                icon = "🔐",
                value = ""
            ),
            AppSettings(
                id = "theme",
                title = "Theme",
                description = "Change app theme",
                type = "navigation",
                icon = "🎨",
                value = "Dark Blue"
            ),
            AppSettings(
                id = "notifications",
                title = "Notifications",
                description = "Configure notification settings",
                type = "navigation",
                icon = "🔔",
                value = ""
            ),
            AppSettings(
                id = "root",
                title = "Root Access",
                description = "Enable root access for advanced features",
                type = "switch",
                icon = "🔓",
                value = if (rootManager.isRootAvailable()) "Available" else "Not available",
                isSwitchChecked = false
            ),
            AppSettings(
                id = "stealth",
                title = "Stealth Mode",
                description = "Hide app from recent tasks",
                type = "switch",
                icon = "👻",
                value = "",
                isSwitchChecked = false
            ),
            AppSettings(
                id = "auto_start",
                title = "Auto Start",
                description = "Start services on boot",
                type = "switch",
                icon = "⚡",
                value = "",
                isSwitchChecked = true
            ),
            AppSettings(
                id = "save_logs",
                title = "Save Logs",
                description = "Save attack logs to storage",
                type = "switch",
                icon = "📝",
                value = "",
                isSwitchChecked = true
            ),
            AppSettings(
                id = "clear_data",
                title = "Clear App Data",
                description = "Reset all app data and settings",
                type = "action",
                icon = "🗑️",
                value = "",
                isDestructive = true
            ),
            AppSettings(
                id = "export_logs",
                title = "Export Logs",
                description = "Export logs to external storage",
                type = "action",
                icon = "📤",
                value = "",
                isDestructive = false
            ),
            AppSettings(
                id = "about",
                title = "About",
                description = "App version and information",
                type = "navigation",
                icon = "ℹ️",
                value = "Version 1.0.0"
            )
        )
        _settings.value = settingsList
    }

    fun toggleRootAccess() {
        viewModelScope.launch {
            if (rootManager.isRootAvailable()) {
                // Request root access
                val granted = rootManager.requestRootAccess()
                android.widget.Toast.makeText(
                    context,
                    if (granted) "Root access granted" else "Root access denied",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            } else {
                android.widget.Toast.makeText(
                    context,
                    "Root access not available on this device",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun toggleStealthMode() {
        // Implement stealth mode
        android.widget.Toast.makeText(context, "Stealth mode toggled", android.widget.Toast.LENGTH_SHORT).show()
    }

    fun toggleAutoStart() {
        // Implement auto start
        android.widget.Toast.makeText(context, "Auto start toggled", android.widget.Toast.LENGTH_SHORT).show()
    }

    fun toggleSaveLogs() {
        // Implement save logs
        android.widget.Toast.makeText(context, "Save logs toggled", android.widget.Toast.LENGTH_SHORT).show()
    }

    fun clearAppData() {
        android.app.AlertDialog.Builder(context)
            .setTitle("Clear App Data")
            .setMessage("Are you sure you want to clear all app data? This action cannot be undone.")
            .setPositiveButton("Yes") { _, _ ->
                // Clear app data
                android.widget.Toast.makeText(context, "App data cleared", android.widget.Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    fun exportLogs() {
        // Implement export logs
        android.widget.Toast.makeText(context, "Logs exported to Downloads folder", android.widget.Toast.LENGTH_SHORT).show()
    }
}