package com.xyrenn.hacking.ui.tools.wifi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemAccessPointBinding
import com.xyrenn.hacking.ui.tools.wifi.models.AccessPoint

class AccessPointAdapter : ListAdapter<AccessPoint, AccessPointAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAccessPointBinding.inflate(
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
        private val binding: ItemAccessPointBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ap: AccessPoint) {
            binding.tvSsid.text = ap.ssid
            binding.tvBssid.text = ap.bssid
            binding.tvChannel.text = "CH ${ap.channel}"
            binding.tvRssi.text = "${ap.rssi} dBm"
            binding.tvEncryption.text = if (ap.isEncrypted) "🔒" else "🔓"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AccessPoint>() {
        override fun areItemsTheSame(oldItem: AccessPoint, newItem: AccessPoint) =
            oldItem.bssid == newItem.bssid

        override fun areContentsTheSame(oldItem: AccessPoint, newItem: AccessPoint) =
            oldItem == newItem
    }
}