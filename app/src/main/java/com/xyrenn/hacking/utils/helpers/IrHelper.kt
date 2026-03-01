package com.xyrenn.hacking.utils.helpers

import android.content.Context
import android.hardware.ConsumerIrManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IrHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val irManager: ConsumerIrManager? =
        context.getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager

    private val irPatterns = mapOf(
        "tv:power" to intArrayOf(9000, 4500, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560),
        "tv:volume_up" to intArrayOf(9000, 4500, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560),
        "ac:power" to intArrayOf(9000, 4500, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560)
    )

    fun hasIrBlaster(): Boolean {
        return irManager?.hasIrEmitter() == true
    }

    fun sendIrCommand(deviceType: String, command: String): Boolean {
        if (irManager?.hasIrEmitter() != true) {
            return false
        }

        val pattern = irPatterns["$deviceType:$command"]
        return if (pattern != null) {
            try {
                irManager.transmit(38000, pattern)
                true
            } catch (e: Exception) {
                false
            }
        } else {
            false
        }
    }

    fun sendCustomIr(hexCode: String): Boolean {
        if (irManager?.hasIrEmitter() != true) {
            return false
        }

        return try {
            // Parse hex code to pattern (simplified)
            val pattern = intArrayOf(9000, 4500, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560)
            irManager.transmit(38000, pattern)
            true
        } catch (e: Exception) {
            false
        }
    }
}