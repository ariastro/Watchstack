package io.sws.myanimetracker.core

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private object AndroidHaptics : KoinComponent {
    private val context: Context by inject()

    fun vibrate(type: HapticFeedbackType) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (!vibrator.hasVibrator()) return
        val effect = when (type) {
            HapticFeedbackType.Light -> VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
            HapticFeedbackType.Medium -> VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE)
            HapticFeedbackType.Success -> VibrationEffect.createWaveform(longArrayOf(0, 25, 40, 25), -1)
            HapticFeedbackType.Warning -> VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
        }
        vibrator.vibrate(effect)
    }
}

actual fun performHaptic(type: HapticFeedbackType) {
    runCatching { AndroidHaptics.vibrate(type) }
}
