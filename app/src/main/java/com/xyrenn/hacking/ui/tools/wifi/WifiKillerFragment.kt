package com.xyrenn.hacking.ui.tools.wifi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentWifiKillerBinding
import com.xyrenn.hacking.utils.helpers.WifiHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WifiKillerFragment : Fragment() {

    private var _binding: FragmentWifiKillerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WifiKillerViewModel by viewModels()
    private var isAttacking = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWifiKillerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnScan.setOnClickListener {
            viewModel.scanNetworks()
        }

        binding.btnAttack.setOnClickListener {
            if (isAttacking) {
                stopAttack()
            } else {
                startAttack()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.networks.observe(viewLifecycleOwner) { networks ->
            // Update UI with networks list
            binding.tvStatus.text = "Found ${networks.size} networks"
        }

        viewModel.isScanning.observe(viewLifecycleOwner) { scanning ->
            binding.btnScan.isEnabled = !scanning
            binding.progressBar.visibility = if (scanning) View.VISIBLE else View.GONE
        }

        viewModel.attackStatus.observe(viewLifecycleOwner) { status ->
            binding.tvAttackStatus.text = status
        }
    }

    private fun startAttack() {
        val targetSsid = binding.etTargetSsid.text.toString()
        if (targetSsid.isEmpty()) {
            Toast.makeText(requireContext(), "Enter target SSID", Toast.LENGTH_SHORT).show()
            return
        }

        isAttacking = true
        binding.btnAttack.text = "STOP ATTACK"
        viewModel.startDeauthAttack(targetSsid)
    }

    private fun stopAttack() {
        isAttacking = false
        binding.btnAttack.text = "START ATTACK"
        viewModel.stopAttack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}