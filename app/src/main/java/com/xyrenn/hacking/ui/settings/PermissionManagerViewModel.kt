package com.xyrenn.hacking.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.settings.models.Permission
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionManagerViewModel @Inject constructor(
    @ApplicationContext private val context: android.content.Context
) : ViewModel() {

    private val _permissions = MutableLiveData<List<Permission>>()
    val permissions: LiveData<List<Permission>> = _permissions

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private val allPermissions = listOf(
        Permission(
            id = "location",
            name = "Location",
            androidPermission = Manifest.permission.ACCESS_FINE_LOCATION,
            description = "Required for WiFi scanning and network discovery",
            rationale = "This app needs location permission to scan for WiFi networks and discover devices on your network."
        ),
        Permission(
            id = "sms",
            name = "SMS",
            androidPermission = Manifest.permission.SEND_SMS,
            description = "Required for SMS spammer functionality",
            rationale = "This app needs SMS permission to send messages for the SMS spammer feature."
        ),
        Permission(
            id = "storage",
            name = "Storage",
            androidPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE,
            description = "Required for saving payloads and logs",
            rationale = "This app needs storage permission to save generated payloads and export logs."
        ),
        Permission(
            id = "camera",
            name = "Camera",
            androidPermission = Manifest.permission.CAMERA,
            description = "Required for QR code scanning",
            rationale = "This app needs camera permission to scan QR codes for device pairing."
        ),
        Permission(
            id = "bluetooth",
            name = "Bluetooth",
            androidPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
                Manifest.permission.BLUETOOTH_SCAN
            else
                Manifest.permission.BLUETOOTH,
            description = "Required for Bluetooth scanning",
            rationale = "This app needs Bluetooth permission to scan for and connect to Bluetooth devices."
        ),
        Permission(
            id = "bluetooth_connect",
            name = "Bluetooth Connect",
            androidPermission = Manifest.permission.BLUETOOTH_CONNECT,
            description = "Required for Bluetooth connection",
            rationale = "This app needs Bluetooth connect permission to establish connections with Bluetooth devices.",
            minSdk = android.os.Build.VERSION_CODES.S
        ),
        Permission(
            id = "bluetooth_advertise",
            name = "Bluetooth Advertise",
            androidPermission = Manifest.permission.BLUETOOTH_ADVERTISE,
            description = "Required for Bluetooth spoofing",
            rationale = "This app needs Bluetooth advertise permission for device spoofing features.",
            minSdk = android.os.Build.VERSION_CODES.S
        ),
        Permission(
            id = "phone",
            name = "Phone",
            androidPermission = Manifest.permission.READ_PHONE_STATE,
            description = "Required for device information",
            rationale = "This app needs phone permission to read device information."
        ),
        Permission(
            id = "notifications",
            name = "Notifications",
            androidPermission = Manifest.permission.POST_NOTIFICATIONS,
            description = "Required for attack notifications",
            rationale = "This app needs notification permission to alert you about attack status.",
            minSdk = android.os.Build.VERSION_CODES.TIRAMISU
        )
    )

    init {
        loadPermissions()
    }

    fun loadPermissions() {
        viewModelScope.launch {
            _status.value = "Checking permissions..."

            val permissionList = allPermissions.map { permission ->
                val isGranted = isPermissionGranted(permission.androidPermission)
                val shouldShowRationale = shouldShowRationale(permission.androidPermission)

                permission.copy(
                    isGranted = isGranted,
                    shouldShowRationale = shouldShowRationale
                )
            }

            _permissions.value = permissionList
            val grantedCount = permissionList.count { it.isGranted }
            _status.value = "$grantedCount/${permissionList.size} permissions granted"
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRationale(permission: String): Boolean {
        return androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
            (context as android.app.Activity),
            permission
        )
    }
}