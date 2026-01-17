package com.example.counter.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.counter.IntroActivity

fun Activity.checkAndShowIntro() {
    val prefs = getSharedPreferences(PREFS_INTRO_NAME, Context.MODE_PRIVATE)
    val launchCount = prefs.getInt(KEY_LAUNCH_COUNT, 0) + 1
    prefs.edit().putInt(KEY_LAUNCH_COUNT, launchCount).apply()

    if (launchCount % 3 == 0) {
        startActivity(Intent(this, IntroActivity::class.java))
    }
}