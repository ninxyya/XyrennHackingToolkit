package com.xyrenn.hacking.ui.tools.network.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemPortBinding
import com.xyrenn.hacking.ui.tools.network.models.PortInfo

class PortAdapter : ListAdapter<PortInfo, PortAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPortBinding.inflate(
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
        private val binding: ItemPortBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(port: PortInfo) {
            binding.tvPort.text = port.port.toString()
            binding.tvService.text = port.service
            binding.tvState.text = port.state
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PortInfo>() {
        override fun areItemsTheSame(oldItem: PortInfo, newItem: PortInfo) =
            oldItem.port == newItem.port

        override fun areContentsTheSame(oldItem: PortInfo, newItem: PortInfo) =
            oldItem == newItem
    }
}