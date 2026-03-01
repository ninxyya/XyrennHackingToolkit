package com.xyrenn.hacking.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.xyrenn.hacking.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatisticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChart()
        observeViewModel()
        viewModel.loadStatistics()
    }

    private fun setupChart() {
        binding.pieChart.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            setEntryLabelColor(android.graphics.Color.WHITE)
            legend.isEnabled = true
            legend.textColor = android.graphics.Color.WHITE
            animateY(1000)
        }
    }

    private fun observeViewModel() {
        viewModel.stats.observe(viewLifecycleOwner) { stats ->
            binding.tvTotalAttacks.text = stats.totalAttacks.toString()
            binding.tvSuccessRate.text = "${stats.successRate}%"
            binding.tvTotalPackets.text = stats.totalPackets.toString()
            binding.tvActiveTime.text = stats.activeTime

            updatePieChart(stats)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun updatePieChart(stats: com.xyrenn.hacking.ui.stats.models.StatData) {
        val entries = listOf(
            PieEntry(stats.wifiAttacks.toFloat(), "WiFi"),
            PieEntry(stats.spamAttacks.toFloat(), "Spam"),
            PieEntry(stats.networkAttacks.toFloat(), "Network"),
            PieEntry(stats.remoteAttacks.toFloat(), "Remote"),
            PieEntry(stats.cryptoAttacks.toFloat(), "Crypto")
        )

        val dataSet = PieDataSet(entries, "Attack Types").apply {
            colors = listOf(
                android.graphics.Color.parseColor("#4F7DF3"),
                android.graphics.Color.parseColor("#F44336"),
                android.graphics.Color.parseColor("#FF9800"),
                android.graphics.Color.parseColor("#9C27B0"),
                android.graphics.Color.parseColor("#4CAF50")
            )
            valueTextColor = android.graphics.Color.WHITE
            valueTextSize = 12f
        }

        binding.pieChart.data = PieData(dataSet)
        binding.pieChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}