package com.xyrenn.hacking.ui.tools.wifi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemClientBinding
import com.xyrenn.hacking.ui.tools.wifi.models.Client

class ClientAdapter(
    private val onItemClick: (Client) -> Unit
) : ListAdapter<Client, ClientAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClientBinding.inflate(
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
        private val binding: ItemClientBinding,
        private val onItemClick: (Client) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(client: Client) {
            binding.tvMac.text = client.mac
            binding.tvIp.text = client.ip
            binding.tvDevice.text = client.deviceName
            binding.tvRssi.text = "${client.rssi} dBm"
            binding.cbSelected.isChecked = client.isSelected

            binding.root.setOnClickListener {
                onItemClick(client)
            }

            binding.cbSelected.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked != client.isSelected) {
                    onItemClick(client)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client) =
            oldItem.mac == newItem.mac

        override fun areContentsTheSame(oldItem: Client, newItem: Client) =
            oldItem == newItem
    }
}