package com.xyrenn.hacking.ui.tools.advanced

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentTermuxEmulatorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermuxEmulatorFragment : Fragment() {

    private var _binding: FragmentTermuxEmulatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TermuxEmulatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTermuxEmulatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnExecute.setOnClickListener {
            val command = binding.etCommand.text.toString()
            if (command.isNotEmpty()) {
                viewModel.executeCommand(command)
                binding.etCommand.text.clear()
            } else {
                Toast.makeText(requireContext(), "Enter command", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnClear.setOnClickListener {
            viewModel.clearOutput()
        }

        binding.btnInstallNmap.setOnClickListener {
            viewModel.installPackage("nmap")
        }

        binding.btnInstallHydra.setOnClickListener {
            viewModel.installPackage("hydra")
        }

        binding.btnInstallSqlmap.setOnClickListener {
            viewModel.installPackage("sqlmap")
        }

        binding.btnInstallMetasploit.setOnClickListener {
            viewModel.installPackage("metasploit")
        }
    }

    private fun observeViewModel() {
        viewModel.output.observe(viewLifecycleOwner) { output ->
            binding.tvOutput.text = output
            binding.scrollView.post {
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }

        viewModel.isExecuting.observe(viewLifecycleOwner) { executing ->
            binding.btnExecute.isEnabled = !executing
            binding.progressBar.visibility = if (executing) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}