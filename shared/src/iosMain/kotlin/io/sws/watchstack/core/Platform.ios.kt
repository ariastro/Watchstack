package io.sws.watchstack.core

import platform.CoreFoundation.CFAbsoluteTimeGetCurrent
import platform.CoreFoundation.kCFAbsoluteTimeIntervalSince1970

actual fun currentTimeMillis(): Long =
    ((CFAbsoluteTimeGetCurrent() + kCFAbsoluteTimeIntervalSince1970) * 1000.0).toLong()
