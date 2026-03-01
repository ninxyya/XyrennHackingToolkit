package com.xyrenn.hacking.ui.tools.spam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentWaSpammerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaSpammerFragment : Fragment() {

    private var _binding: FragmentWaSpammerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WaSpammerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaSpammerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnSendWa.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val message = binding.etMessage.text.toString()
            val count = binding.etCount.text.toString().toIntOrNull() ?: 1

            if (phoneNumber.isNotEmpty() && message.isNotEmpty()) {
                viewModel.sendWhatsApp(phoneNumber, message, count)
            } else {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStop.setOnClickListener {
            viewModel.stopSending()
        }

        binding.btnOpenWhatsApp.setOnClickListener {
            viewModel.openWhatsApp(requireContext(), binding.etPhoneNumber.text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.isSending.observe(viewLifecycleOwner) { sending ->
            binding.btnSendWa.isEnabled = !sending
            binding.btnStop.isEnabled = sending
            binding.progressBar.visibility = if (sending) View.VISIBLE else View.GONE
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
        }

        viewModel.sentCount.observe(viewLifecycleOwner) { count ->
            binding.tvSentCount.text = "Sent: $count"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}