package com.xyrenn.hacking.ui.tools.wifi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentWifiScannerBinding
import com.xyrenn.hacking.ui.tools.wifi.adapters.AccessPointAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WifiScannerFragment : Fragment() {

    private var _binding: FragmentWifiScannerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WifiScannerViewModel by viewModels()
    private lateinit var adapter: AccessPointAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWifiScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = AccessPointAdapter()
        binding.rvNetworks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNetworks.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnStartScan.setOnClickListener {
            viewModel.startScan()
        }

        binding.btnStopScan.setOnClickListener {
            viewModel.stopScan()
        }
    }

    private fun observeViewModel() {
        viewModel.networks.observe(viewLifecycleOwner) { networks ->
            adapter.submitList(networks)
            binding.tvNetworkCount.text = "Networks: ${networks.size}"
        }

        viewModel.isScanning.observe(viewLifecycleOwner) { scanning ->
            binding.btnStartScan.isEnabled = !scanning
            binding.btnStopScan.isEnabled = scanning
            binding.progressBar.visibility = if (scanning) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}