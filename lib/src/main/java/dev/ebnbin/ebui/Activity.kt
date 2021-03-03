package dev.ebnbin.ebui

import android.app.Activity
import dev.ebnbin.eb.getValue
import dev.ebnbin.eb.hasKey
import dev.ebnbin.eb.notNull

fun Activity.hasExtraKey(key: String): Boolean {
    return intent?.extras?.hasKey(key) ?: false
}

inline fun <reified T : Any> Activity.getExtra(key: String): T? {
    return intent?.extras?.getValue(key)
}

inline fun <reified T : Any> Activity.requireExtra(key: String): T {
    return getExtra<T>(key).notNull()
}

inline fun <reified T : Any> Activity.getExtraOrDefault(key: String, defaultValue: T): T {
    return if (hasExtraKey(key)) requireExtra(key) else defaultValue
}
