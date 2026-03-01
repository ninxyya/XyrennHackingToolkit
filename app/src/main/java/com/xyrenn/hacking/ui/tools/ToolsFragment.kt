package com.xyrenn.hacking.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.xyrenn.hacking.databinding.FragmentToolsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToolsFragment : Fragment() {
    
    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ToolsViewModel by viewModels()
    private lateinit var adapter: ToolsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeData()
    }
    
    private fun setupRecyclerView() {
        adapter = ToolsAdapter { tool ->
            // Handle tool click
        }
        binding.rvTools.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvTools.adapter = adapter
    }
    
    private fun observeData() {
        viewModel.tools.observe(viewLifecycleOwner) { tools ->
            adapter.submitList(tools)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}