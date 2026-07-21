package io.sws.myanimetracker.core

enum class HapticFeedbackType {
    Light,
    Medium,
    Success,
    Warning
}

expect fun performHaptic(type: HapticFeedbackType = HapticFeedbackType.Light)
