package com.xyrenn.hacking.ui.tools.advanced.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemScriptBinding
import com.xyrenn.hacking.ui.tools.advanced.models.Script

class ScriptAdapter(
    private val onItemClick: (Script) -> Unit
) : ListAdapter<Script, ScriptAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScriptBinding.inflate(
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
        private val binding: ItemScriptBinding,
        private val onItemClick: (Script) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(script: Script) {
            binding.tvScriptName.text = script.name
            binding.tvScriptLanguage.text = script.language
            binding.tvScriptPreview.text = script.content.take(50) + "..."

            binding.root.setOnClickListener {
                onItemClick(script)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Script>() {
        override fun areItemsTheSame(oldItem: Script, newItem: Script) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Script, newItem: Script) =
            oldItem == newItem
    }
}