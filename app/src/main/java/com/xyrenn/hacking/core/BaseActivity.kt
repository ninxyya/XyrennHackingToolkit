package com.xyrenn.hacking.core

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract fun inflateBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding()
        setContentView(binding.root)
        
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

    protected fun showLoading() {
        // Override in child activities to show loading
    }

    protected fun hideLoading() {
        // Override in child activities to hide loading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}