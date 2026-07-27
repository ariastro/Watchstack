package io.sws.watchstack.core

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType

actual fun performHaptic(type: HapticFeedbackType) {
    when (type) {
        HapticFeedbackType.Light -> {
            val gen = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
            gen.prepare()
            gen.impactOccurred()
        }
        HapticFeedbackType.Medium -> {
            val gen = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
            gen.prepare()
            gen.impactOccurred()
        }
        HapticFeedbackType.Success -> {
            val gen = UINotificationFeedbackGenerator()
            gen.prepare()
            gen.notificationOccurred(UINotificationFeedbackType.UINotificationFeedbackTypeSuccess)
        }
        HapticFeedbackType.Warning -> {
            val gen = UINotificationFeedbackGenerator()
            gen.prepare()
            gen.notificationOccurred(UINotificationFeedbackType.UINotificationFeedbackTypeWarning)
        }
    }
}
