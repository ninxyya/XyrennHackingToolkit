package com.xyrenn.hacking.ui.settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemSettingBinding
import com.xyrenn.hacking.ui.settings.models.AppSettings

class SettingsAdapter(
    private val onItemClick: (AppSettings) -> Unit
) : ListAdapter<AppSettings, SettingsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSettingBinding.inflate(
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
        private val binding: ItemSettingBinding,
        private val onItemClick: (AppSettings) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(setting: AppSettings) {
            binding.tvSettingTitle.text = setting.title
            binding.tvSettingDescription.text = setting.description
            binding.tvSettingIcon.text = setting.icon

            when (setting.type) {
                "navigation" -> {
                    binding.switchSetting.visibility = android.view.View.GONE
                    binding.tvSettingValue.visibility = android.view.View.VISIBLE
                    binding.tvSettingValue.text = setting.value
                    binding.root.setOnClickListener { onItemClick(setting) }
                }
                "switch" -> {
                    binding.switchSetting.visibility = android.view.View.VISIBLE
                    binding.tvSettingValue.visibility = android.view.View.GONE
                    binding.switchSetting.isChecked = setting.isSwitchChecked
                    binding.switchSetting.setOnCheckedChangeListener { _, isChecked ->
                        // Handle switch toggle
                        onItemClick(setting)
                    }
                    binding.root.setOnClickListener {
                        binding.switchSetting.toggle()
                    }
                }
                "action" -> {
                    binding.switchSetting.visibility = android.view.View.GONE
                    binding.tvSettingValue.visibility = android.view.View.VISIBLE
                    binding.tvSettingValue.text = setting.value
                    binding.root.setOnClickListener { onItemClick(setting) }
                }
            }

            if (setting.isDestructive) {
                binding.tvSettingTitle.setTextColor(android.graphics.Color.parseColor("#F44336"))
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AppSettings>() {
        override fun areItemsTheSame(oldItem: AppSettings, newItem: AppSettings) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AppSettings, newItem: AppSettings) =
            oldItem == newItem
    }
}