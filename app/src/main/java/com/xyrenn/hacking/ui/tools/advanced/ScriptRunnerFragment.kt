package com.xyrenn.hacking.ui.tools.advanced

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentScriptRunnerBinding
import com.xyrenn.hacking.ui.tools.advanced.adapters.ScriptAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScriptRunnerFragment : Fragment() {

    private var _binding: FragmentScriptRunnerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScriptRunnerViewModel by viewModels()
    private lateinit var scriptAdapter: ScriptAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScriptRunnerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        scriptAdapter = ScriptAdapter { script ->
            viewModel.selectScript(script)
        }
        binding.rvScripts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScripts.adapter = scriptAdapter
    }

    private fun setupClickListeners() {
        binding.btnLoadScripts.setOnClickListener {
            viewModel.loadScripts()
        }

        binding.btnRunScript.setOnClickListener {
            val params = binding.etParams.text.toString()
            viewModel.runSelectedScript(params)
        }

        binding.btnStopScript.setOnClickListener {
            viewModel.stopScript()
        }

        binding.btnSaveScript.setOnClickListener {
            val name = binding.etScriptName.text.toString()
            val content = binding.etScriptContent.text.toString()

            if (name.isNotEmpty() && content.isNotEmpty()) {
                viewModel.saveScript(name, content)
                binding.etScriptName.text.clear()
                binding.etScriptContent.text.clear()
            } else {
                Toast.makeText(requireContext(), "Enter script name and content", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDeleteScript.setOnClickListener {
            viewModel.deleteSelectedScript()
        }
    }

    private fun observeViewModel() {
        viewModel.scripts.observe(viewLifecycleOwner) { scripts ->
            scriptAdapter.submitList(scripts)
        }

        viewModel.selectedScript.observe(viewLifecycleOwner) { script ->
            if (script != null) {
                binding.tvSelectedScript.text = "Selected: ${script.name}"
                binding.etScriptContent.setText(script.content)
                binding.btnRunScript.isEnabled = true
                binding.btnDeleteScript.isEnabled = true
            } else {
                binding.tvSelectedScript.text = "No script selected"
                binding.btnRunScript.isEnabled = false
                binding.btnDeleteScript.isEnabled = false
            }
        }

        viewModel.isRunning.observe(viewLifecycleOwner) { running ->
            binding.btnRunScript.isEnabled = !running && viewModel.selectedScript.value != null
            binding.btnStopScript.isEnabled = running
            binding.progressBar.visibility = if (running) View.VISIBLE else View.GONE
        }

        viewModel.output.observe(viewLifecycleOwner) { output ->
            binding.tvOutput.text = output
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}