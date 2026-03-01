package com.xyrenn.hacking.ui.tools.bluetooth.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemBluetoothDeviceBinding
import com.xyrenn.hacking.ui.tools.bluetooth.models.BluetoothDeviceInfo

class BluetoothDeviceAdapter(
    private val onItemClick: (BluetoothDeviceInfo) -> Unit
) : ListAdapter<BluetoothDeviceInfo, BluetoothDeviceAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBluetoothDeviceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemBluetoothDeviceBinding,
        private val onItemClick: (BluetoothDeviceInfo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(device: BluetoothDeviceInfo) {
            binding.tvDeviceName.text = device.name
            binding.tvDeviceAddress.text = device.address
            binding.tvDeviceRssi.text = "${device.rssi} dBm"
            binding.tvDeviceBond.text = device.getBondStateString()

            // Signal strength indicator
            val signalStrength = when {
                device.rssi > -50 -> "Strong"
                device.rssi > -70 -> "Good"
                device.rssi > -85 -> "Fair"
                else -> "Weak"
            }
            binding.tvSignalStrength.text = signalStrength

            binding.root.setOnClickListener {
                onItemClick(device)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BluetoothDeviceInfo>() {
        override fun areItemsTheSame(oldItem: BluetoothDeviceInfo, newItem: BluetoothDeviceInfo) =
            oldItem.address == newItem.address

        override fun areContentsTheSame(oldItem: BluetoothDeviceInfo, newItem: BluetoothDeviceInfo) =
            oldItem == newItem
    }
}