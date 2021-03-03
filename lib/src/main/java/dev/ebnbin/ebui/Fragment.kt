package dev.ebnbin.ebui

import androidx.fragment.app.Fragment
import dev.ebnbin.eb.getValue
import dev.ebnbin.eb.hasKey
import dev.ebnbin.eb.notNull

fun Fragment.hasArgKey(key: String): Boolean {
    return arguments?.hasKey(key) ?: false
}

inline fun <reified T : Any> Fragment.getArg(key: String): T? {
    return arguments?.getValue(key)
}

inline fun <reified T : Any> Fragment.requireArg(key: String): T {
    return getArg<T>(key).notNull()
}

inline fun <reified T : Any> Fragment.getArgOrDefault(key: String, defaultValue: T): T {
    return if (hasArgKey(key)) requireArg(key) else defaultValue
}
