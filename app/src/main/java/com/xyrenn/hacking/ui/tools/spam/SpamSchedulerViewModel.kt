package com.xyrenn.hacking.ui.tools.spam

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.receivers.AlarmReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SpamSchedulerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _selectedTime = MutableLiveData<String?>()
    val selectedTime: LiveData<String?> = _selectedTime

    private val _isScheduled = MutableLiveData(false)
    val isScheduled: LiveData<Boolean> = _isScheduled

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private var selectedHour = 0
    private var selectedMinute = 0

    fun setSelectedTime(hour: Int, minute: Int) {
        selectedHour = hour
        selectedMinute = minute
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        _selectedTime.value = timeFormat.format(calendar.time)
    }

    fun scheduleSpam(phoneNumber: String, message: String, count: Int) {
        viewModelScope.launch {
            try {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("phone_number", phoneNumber)
                    putExtra("message", message)
                    putExtra("count", count)
                }
                
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                    
                    // If time is in past, schedule for tomorrow
                    if (timeInMillis <= System.currentTimeMillis()) {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }
                }

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )

                _isScheduled.value = true
                _status.value = "Scheduled at ${_selectedTime.value}"
                
            } catch (e: Exception) {
                _status.value = "Failed to schedule: ${e.message}"
            }
        }
    }

    fun cancelSchedule() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        
        _isScheduled.value = false
        _selectedTime.value = null
        _status.value = "Schedule cancelled"
    }
}