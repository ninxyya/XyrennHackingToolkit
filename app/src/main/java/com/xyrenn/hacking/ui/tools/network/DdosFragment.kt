package com.xyrenn.hacking.ui.tools.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentDdosBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DdosFragment : Fragment() {

    private var _binding: FragmentDdosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DdosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDdosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnStartAttack.setOnClickListener {
            val targetIp = binding.etTargetIp.text.toString()
            val port = binding.etPort.text.toString().toIntOrNull() ?: 80
            val duration = binding.etDuration.text.toString().toIntOrNull() ?: 10
            val threads = binding.etThreads.text.toString().toIntOrNull() ?: 5

            if (targetIp.isNotEmpty()) {
                viewModel.startDdosAttack(targetIp, port, duration, threads)
            } else {
                Toast.makeText(requireContext(), "Enter target IP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStopAttack.setOnClickListener {
            viewModel.stopAttack()
        }
    }

    private fun observeViewModel() {
        viewModel.isAttacking.observe(viewLifecycleOwner) { attacking ->
            binding.btnStartAttack.isEnabled = !attacking
            binding.btnStopAttack.isEnabled = attacking
            binding.progressBar.visibility = if (attacking) View.VISIBLE else View.GONE
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
        }

        viewModel.packetsSent.observe(viewLifecycleOwner) { packets ->
            binding.tvPacketsSent.text = "Packets: $packets"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}