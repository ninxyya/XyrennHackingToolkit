package com.xyrenn.hacking.ui.tools.wifi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentDeautherBinding
import com.xyrenn.hacking.ui.tools.wifi.adapters.ClientAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeautherFragment : Fragment() {

    private var _binding: FragmentDeautherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DeautherViewModel by viewModels()
    private lateinit var clientAdapter: ClientAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeautherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSpinner()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        clientAdapter = ClientAdapter { client ->
            viewModel.toggleClientSelection(client)
        }
        binding.rvClients.layoutManager = LinearLayoutManager(requireContext())
        binding.rvClients.adapter = clientAdapter
    }

    private fun setupSpinner() {
        val channels = (1..13).toList().map { "Channel $it" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, channels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerChannel.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnScanClients.setOnClickListener {
            val targetSsid = binding.etTargetSsid.text.toString()
            if (targetSsid.isNotEmpty()) {
                viewModel.scanClients(targetSsid)
            } else {
                Toast.makeText(requireContext(), "Enter target SSID", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDeauth.setOnClickListener {
            viewModel.startDeauthSelected()
        }

        binding.btnDeauthAll.setOnClickListener {
            viewModel.startDeauthAll()
        }

        binding.btnStop.setOnClickListener {
            viewModel.stopDeauth()
        }
    }

    private fun observeViewModel() {
        viewModel.clients.observe(viewLifecycleOwner) { clients ->
            clientAdapter.submitList(clients)
            binding.tvClientCount.text = "Clients: ${clients.size}"
        }

        viewModel.isScanning.observe(viewLifecycleOwner) { scanning ->
            binding.btnScanClients.isEnabled = !scanning
            binding.progressBar.visibility = if (scanning) View.VISIBLE else View.GONE
        }

        viewModel.attackStatus.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}