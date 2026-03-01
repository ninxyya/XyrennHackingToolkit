package com.xyrenn.hacking.ui.tools.wifi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentWifiAnalyzerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WifiAnalyzerFragment : Fragment() {

    private var _binding: FragmentWifiAnalyzerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WifiAnalyzerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWifiAnalyzerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnAnalyze.setOnClickListener {
            viewModel.analyzeChannels()
        }
    }

    private fun observeViewModel() {
        viewModel.channelAnalysis.observe(viewLifecycleOwner) { analysis ->
            binding.tvChannel1.text = "Channel 1: ${analysis.channel1}"
            binding.tvChannel6.text = "Channel 6: ${analysis.channel6}"
            binding.tvChannel11.text = "Channel 11: ${analysis.channel11}"
        }

        viewModel.isAnalyzing.observe(viewLifecycleOwner) { analyzing ->
            binding.btnAnalyze.isEnabled = !analyzing
            binding.progressBar.visibility = if (analyzing) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}