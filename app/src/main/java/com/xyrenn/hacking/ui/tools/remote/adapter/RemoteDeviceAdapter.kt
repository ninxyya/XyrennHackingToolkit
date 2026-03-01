package com.xyrenn.hacking.ui.tools.remote.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemRemoteDeviceBinding
import com.xyrenn.hacking.ui.tools.remote.models.RemoteDevice

class RemoteDeviceAdapter(
    private val onItemClick: (RemoteDevice) -> Unit
) : ListAdapter<RemoteDevice, RemoteDeviceAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRemoteDeviceBinding.inflate(
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
        private val binding: ItemRemoteDeviceBinding,
        private val onItemClick: (RemoteDevice) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(device: RemoteDevice) {
            binding.tvDeviceName.text = device.name
            binding.tvDeviceType.text = device.type
            binding.tvDeviceIp.text = device.ip
            binding.tvStatus.text = if (device.isAvailable) "Available" else "Offline"

            binding.root.setOnClickListener {
                onItemClick(device)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RemoteDevice>() {
        override fun areItemsTheSame(oldItem: RemoteDevice, newItem: RemoteDevice) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RemoteDevice, newItem: RemoteDevice) =
            oldItem == newItem
    }
}