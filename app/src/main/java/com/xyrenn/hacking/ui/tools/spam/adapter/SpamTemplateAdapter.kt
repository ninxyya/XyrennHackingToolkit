package com.xyrenn.hacking.ui.tools.spam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemSpamTemplateBinding
import com.xyrenn.hacking.ui.tools.spam.models.SpamTemplate

class SpamTemplateAdapter(
    private val onItemClick: (SpamTemplate) -> Unit
) : ListAdapter<SpamTemplate, SpamTemplateAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSpamTemplateBinding.inflate(
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
        private val binding: ItemSpamTemplateBinding,
        private val onItemClick: (SpamTemplate) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(template: SpamTemplate) {
            binding.tvTemplateName.text = template.name
            binding.tvTemplateContent.text = template.content

            binding.root.setOnClickListener {
                onItemClick(template)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SpamTemplate>() {
        override fun areItemsTheSame(oldItem: SpamTemplate, newItem: SpamTemplate) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SpamTemplate, newItem: SpamTemplate) =
            oldItem == newItem
    }
}