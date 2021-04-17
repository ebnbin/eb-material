package dev.ebnbin.ebui

import androidx.appcompat.app.AppCompatDelegate
import dev.ebnbin.eb.Pref
import dev.ebnbin.eb.Prefs
import dev.ebnbin.eb.appId

object EBUIPrefs : Prefs() {
    override val prefName: String = "$appId.ebui"

    val nightMode: Pref<Int> = createPref("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
}
