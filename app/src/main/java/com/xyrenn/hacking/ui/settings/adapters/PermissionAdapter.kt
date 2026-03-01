package com.xyrenn.hacking.ui.settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemPermissionBinding
import com.xyrenn.hacking.ui.settings.models.Permission

class PermissionAdapter(
    private val onItemClick: (Permission) -> Unit
) : ListAdapter<Permission, PermissionAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPermissionBinding.inflate(
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
        private val binding: ItemPermissionBinding,
        private val onItemClick: (Permission) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(permission: Permission) {
            binding.tvPermissionName.text = permission.name
            binding.tvPermissionDesc.text = permission.description

            if (permission.isGranted) {
                binding.tvPermissionStatus.text = "Granted"
                binding.tvPermissionStatus.setTextColor(android.graphics.Color.GREEN)
                binding.btnAction.text = "OK"
            } else {
                binding.tvPermissionStatus.text = "Not Granted"
                binding.tvPermissionStatus.setTextColor(android.graphics.Color.RED)
                binding.btnAction.text = "GRANT"
            }

            binding.btnAction.setOnClickListener {
                onItemClick(permission)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Permission>() {
        override fun areItemsTheSame(oldItem: Permission, newItem: Permission) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Permission, newItem: Permission) =
            oldItem == newItem
    }
}