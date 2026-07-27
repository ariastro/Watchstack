package io.sws.watchstack.core

enum class HapticFeedbackType {
    Light,
    Medium,
    Success,
    Warning
}

expect fun performHaptic(type: HapticFeedbackType = HapticFeedbackType.Light)
