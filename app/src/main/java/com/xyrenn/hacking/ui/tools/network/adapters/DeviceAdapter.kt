package com.xyrenn.hacking.ui.tools.network.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemNetworkDeviceBinding
import com.xyrenn.hacking.ui.tools.network.models.NetworkDevice

class DeviceAdapter : ListAdapter<NetworkDevice, DeviceAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNetworkDeviceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemNetworkDeviceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(device: NetworkDevice) {
            binding.tvIp.text = device.ip
            binding.tvMac.text = device.mac
            binding.tvHostname.text = device.hostname
            binding.tvVendor.text = device.vendor
            binding.tvOpenPorts.text = "Open ports: ${device.openPorts.size}"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<NetworkDevice>() {
        override fun areItemsTheSame(oldItem: NetworkDevice, newItem: NetworkDevice) =
            oldItem.ip == newItem.ip

        override fun areContentsTheSame(oldItem: NetworkDevice, newItem: NetworkDevice) =
            oldItem == newItem
    }
}