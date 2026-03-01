package com.xyrenn.hacking.ui.tools.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentIrBlasterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IRBlasterFragment : Fragment() {

    private var _binding: FragmentIrBlasterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: IRBlasterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIrBlasterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        // TV Buttons
        binding.btnTvPower.setOnClickListener { viewModel.sendIrCommand("tv", "power") }
        binding.btnTvVolUp.setOnClickListener { viewModel.sendIrCommand("tv", "volume_up") }
        binding.btnTvVolDown.setOnClickListener { viewModel.sendIrCommand("tv", "volume_down") }
        binding.btnTvChUp.setOnClickListener { viewModel.sendIrCommand("tv", "channel_up") }
        binding.btnTvChDown.setOnClickListener { viewModel.sendIrCommand("tv", "channel_down") }
        binding.btnTvMute.setOnClickListener { viewModel.sendIrCommand("tv", "mute") }

        // AC Buttons
        binding.btnAcPower.setOnClickListener { viewModel.sendIrCommand("ac", "power") }
        binding.btnAcTempUp.setOnClickListener { viewModel.sendIrCommand("ac", "temp_up") }
        binding.btnAcTempDown.setOnClickListener { viewModel.sendIrCommand("ac", "temp_down") }
        binding.btnAcMode.setOnClickListener { viewModel.sendIrCommand("ac", "mode") }
        binding.btnAcFan.setOnClickListener { viewModel.sendIrCommand("ac", "fan") }

        // Other Devices
        binding.btnSoundbarPower.setOnClickListener { viewModel.sendIrCommand("soundbar", "power") }
        binding.btnSoundbarVolUp.setOnClickListener { viewModel.sendIrCommand("soundbar", "volume_up") }
        binding.btnSoundbarVolDown.setOnClickListener { viewModel.sendIrCommand("soundbar", "volume_down") }

        binding.btnProjectorPower.setOnClickListener { viewModel.sendIrCommand("projector", "power") }
        binding.btnProjectorInput.setOnClickListener { viewModel.sendIrCommand("projector", "input") }

        binding.btnFanPower.setOnClickListener { viewModel.sendIrCommand("fan", "power") }
        binding.btnFanSpeed.setOnClickListener { viewModel.sendIrCommand("fan", "speed") }

        binding.btnCustomIr.setOnClickListener {
            val hexCode = binding.etCustomHex.text.toString()
            if (hexCode.isNotEmpty()) {
                viewModel.sendCustomIr(hexCode)
                binding.etCustomHex.text.clear()
            } else {
                Toast.makeText(requireContext(), "Enter HEX code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.lastCommand.observe(viewLifecycleOwner) { command ->
            binding.tvLastCommand.text = "Last: $command"
        }

        viewModel.hasIrBlaster.observe(viewLifecycleOwner) { hasIr ->
            if (!hasIr) {
                binding.tvIrStatus.text = "IR Blaster not available on this device"
                binding.tvIrStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}