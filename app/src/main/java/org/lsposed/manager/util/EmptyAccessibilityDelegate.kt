package org.lsposed.manager.util

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeProvider

class EmptyAccessibilityDelegate : View.AccessibilityDelegate() {
    override fun sendAccessibilityEvent(host: View, eventType: Int) {}

    override fun performAccessibilityAction(host: View, action: Int, args: Bundle?): Boolean = true

    override fun sendAccessibilityEventUnchecked(host: View, event: AccessibilityEvent) {}

    override fun dispatchPopulateAccessibilityEvent(host: View, event: AccessibilityEvent): Boolean = true

    override fun onPopulateAccessibilityEvent(host: View, event: AccessibilityEvent) {}

    override fun onInitializeAccessibilityEvent(host: View, event: AccessibilityEvent) {}

    override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {}

    override fun addExtraDataToAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo, extraDataKey: String, arguments: Bundle?) {}

    override fun onRequestSendAccessibilityEvent(host: ViewGroup, child: View, event: AccessibilityEvent): Boolean = true

    override fun getAccessibilityNodeProvider(host: View): AccessibilityNodeProvider? = null
}