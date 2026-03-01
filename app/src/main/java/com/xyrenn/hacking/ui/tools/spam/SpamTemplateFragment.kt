package com.xyrenn.hacking.ui.tools.spam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xyrenn.hacking.databinding.FragmentSpamTemplateBinding
import com.xyrenn.hacking.ui.tools.spam.adapters.SpamTemplateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpamTemplateFragment : Fragment() {

    private var _binding: FragmentSpamTemplateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SpamTemplateViewModel by viewModels()
    private lateinit var adapter: SpamTemplateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpamTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = SpamTemplateAdapter { template ->
            binding.etMessage.setText(template.content)
        }
        binding.rvTemplates.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTemplates.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnSaveTemplate.setOnClickListener {
            val name = binding.etTemplateName.text.toString()
            val content = binding.etMessage.text.toString()

            if (name.isNotEmpty() && content.isNotEmpty()) {
                viewModel.saveTemplate(name, content)
                binding.etTemplateName.text.clear()
                binding.etMessage.text.clear()
            } else {
                Toast.makeText(requireContext(), "Fill template name and message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.templates.observe(viewLifecycleOwner) { templates ->
            adapter.submitList(templates)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}