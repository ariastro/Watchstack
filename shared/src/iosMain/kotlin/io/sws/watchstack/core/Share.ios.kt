package io.sws.watchstack.core

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

actual fun shareText(text: String, title: String?) {
    val items = listOf(text)
    val controller = UIActivityViewController(activityItems = items, applicationActivities = null)
    val root = UIApplication.sharedApplication.keyWindow?.rootViewController
        ?: return
    val presenter = topViewController(root)
    presenter.presentViewController(controller, animated = true, completion = null)
}

private fun topViewController(root: UIViewController): UIViewController {
    var current = root
    while (true) {
        val presented = current.presentedViewController ?: break
        current = presented
    }
    return current
}
