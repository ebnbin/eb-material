package dev.ebnbin.ebui

import androidx.appcompat.app.AppCompatDelegate
import dev.ebnbin.eb.Pref
import dev.ebnbin.eb.appId

object EBUIPrefs {
    private val name: String = "$appId.ebui"

    val night_mode: Pref<Int> = Pref.create("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, name)
}
