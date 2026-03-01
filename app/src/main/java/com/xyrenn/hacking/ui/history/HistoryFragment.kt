package com.xyrenn.hacking.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentHistoryBinding
import com.xyrenn.hacking.ui.history.adapters.HistoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        viewModel.loadHistory()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter { log ->
            viewModel.selectLog(log)
        }
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = historyAdapter
    }

    private fun setupClickListeners() {
        binding.btnFilter.setOnClickListener {
            showFilterDialog()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.btnRefresh.setOnClickListener {
            viewModel.loadHistory()
        }
    }

    private fun showFilterDialog() {
        val options = arrayOf("All", "WiFi Attacks", "Spam", "DDoS", "Remote", "Crypto")
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Filter by Type")
            .setItems(options) { _, which ->
                viewModel.filterByType(options[which])
            }
            .show()
    }

    private fun observeViewModel() {
        viewModel.historyLogs.observe(viewLifecycleOwner) { logs ->
            historyAdapter.submitList(logs)
            binding.tvLogCount.text = "Total: ${logs.size}"
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
        }

        viewModel.selectedLog.observe(viewLifecycleOwner) { log ->
            if (log != null) {
                showLogDetails(log)
            }
        }
    }

    private fun showLogDetails(log: com.xyrenn.hacking.ui.history.models.AttackLog) {
        val details = """
            Type: ${log.type}
            Target: ${log.target}
            Status: ${log.status}
            Start Time: ${log.startTime}
            End Time: ${log.endTime}
            Duration: ${log.duration} seconds
            Details: ${log.details}
        """.trimIndent()

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Attack Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}