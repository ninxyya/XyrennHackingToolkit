package com.xyrenn.hacking.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
        loadData()
    }

    protected open fun setupUI() {}

    protected open fun setupObservers() {}

    protected open fun loadData() {}

    protected fun showMessage(message: String) {
        lifecycleScope.launch {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    protected fun showError(message: String) {
        lifecycleScope.launch {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(resources.getColor(android.R.color.holo_red_dark))
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}