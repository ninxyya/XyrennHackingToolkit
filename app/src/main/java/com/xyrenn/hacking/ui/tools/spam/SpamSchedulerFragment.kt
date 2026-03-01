package com.xyrenn.hacking.ui.tools.spam

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xyrenn.hacking.databinding.FragmentSpamSchedulerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SpamSchedulerFragment : Fragment() {

    private var _binding: FragmentSpamSchedulerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SpamSchedulerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpamSchedulerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnSelectTime.setOnClickListener {
            showTimePicker()
        }

        binding.btnSchedule.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val message = binding.etMessage.text.toString()
            val count = binding.etCount.text.toString().toIntOrNull() ?: 1

            if (phoneNumber.isNotEmpty() && message.isNotEmpty() && viewModel.selectedTime.value != null) {
                viewModel.scheduleSpam(phoneNumber, message, count)
            } else {
                Toast.makeText(requireContext(), "Fill all fields and select time", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancelSchedule.setOnClickListener {
            viewModel.cancelSchedule()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            viewModel.setSelectedTime(selectedHour, selectedMinute)
        }, hour, minute, true).show()
    }

    private fun observeViewModel() {
        viewModel.selectedTime.observe(viewLifecycleOwner) { time ->
            binding.tvSelectedTime.text = "Schedule at: $time"
        }

        viewModel.isScheduled.observe(viewLifecycleOwner) { scheduled ->
            binding.btnSchedule.isEnabled = !scheduled
            binding.btnCancelSchedule.isEnabled = scheduled
            binding.progressBar.visibility = if (scheduled) View.VISIBLE else View.GONE
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}