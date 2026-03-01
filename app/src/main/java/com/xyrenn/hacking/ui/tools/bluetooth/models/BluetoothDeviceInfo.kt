package com.xyrenn.hacking.ui.tools.bluetooth.models

import android.bluetooth.BluetoothDevice

data class BluetoothDeviceInfo(
    val name: String,
    val address: String,
    val rssi: Int,
    val bondState: Int,
    val isSelected: Boolean = false
) {
    fun getBondStateString(): String {
        return when (bondState) {
            BluetoothDevice.BOND_BONDED -> "Paired"
            BluetoothDevice.BOND_BONDING -> "Pairing"
            else -> "Not paired"
        }
    }
}