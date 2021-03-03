package dev.ebnbin.ebui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import dev.ebnbin.eb.notNull

/**
 * Get Activity by Context.
 */
tailrec fun Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        else -> (this as? ContextWrapper)?.baseContext?.getActivity()
    }
}

fun Context.requireActivity(): Activity {
    return getActivity().notNull()
}

inline fun <reified T : Activity> Context.getTActivity(): T? {
    return getActivity() as? T
}

inline fun <reified T : Activity> Context.requireTActivity(): T {
    return getTActivity<T>().notNull()
}
