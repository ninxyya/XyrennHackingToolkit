package com.xyrenn.hacking.ui.history.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xyrenn.hacking.databinding.ItemHistoryBinding
import com.xyrenn.hacking.ui.history.models.AttackLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (AttackLog) -> Unit
) : ListAdapter<AttackLog, HistoryAdapter.ViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick, dateFormat)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemHistoryBinding,
        private val onItemClick: (AttackLog) -> Unit,
        private val dateFormat: SimpleDateFormat
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(log: AttackLog) {
            binding.tvAttackType.text = log.type
            binding.tvAttackTarget.text = "Target: ${log.target}"
            binding.tvAttackStatus.text = log.status
            binding.tvAttackTime.text = dateFormat.format(Date(log.startTime))
            binding.tvAttackDuration.text = "Duration: ${log.duration}s"

            // Set status color
            val statusColor = when (log.status) {
                "Success" -> android.graphics.Color.GREEN
                "Partial" -> android.graphics.Color.YELLOW
                "Failed" -> android.graphics.Color.RED
                else -> android.graphics.Color.GRAY
            }
            binding.tvAttackStatus.setTextColor(statusColor)

            binding.root.setOnClickListener {
                onItemClick(log)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AttackLog>() {
        override fun areItemsTheSame(oldItem: AttackLog, newItem: AttackLog) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AttackLog, newItem: AttackLog) =
            oldItem == newItem
    }
}