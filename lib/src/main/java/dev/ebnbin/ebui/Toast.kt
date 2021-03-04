package dev.ebnbin.ebui

import android.widget.Toast
import dev.ebnbin.eb.app
import java.lang.ref.WeakReference

private var toastRef: WeakReference<Toast>? = null

fun toast(any: Any?, durationLong: Boolean = false) {
    toastRef?.get()?.let {
        toastRef = null
        it.cancel()
    }
    val text = if (any is Int) {
        app.getText(any)
    } else {
        any.toString()
    }
    val duration = if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(app, text, duration).also {
        toastRef = WeakReference(it)
    }.show()
}
