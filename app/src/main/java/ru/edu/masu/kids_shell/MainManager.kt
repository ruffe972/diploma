package ru.edu.masu.kids_shell

import android.content.Context
import android.graphics.drawable.Drawable

object MainManager {
    val appsManager = AppsManager(context())

    fun context(): Context = app().applicationContext
    fun getDrawable(id: Int): Drawable = context().getDrawable(id) ?: appIcon()

    private fun app() = App.instance
    private fun appIcon() = context().packageManager
            .getApplicationIcon(BuildConfig.APPLICATION_ID)
}