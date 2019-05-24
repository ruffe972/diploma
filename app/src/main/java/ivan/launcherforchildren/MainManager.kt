package ivan.launcherforchildren

import android.content.Context
import android.graphics.drawable.Drawable

object MainManager {
    val appsManager = AppsManager(appContext())

    fun appContext(): Context = app().applicationContext
    fun getDrawable(id: Int): Drawable = appContext().getDrawable(id) ?: appIcon()
    private fun app() = App.instance
    private fun appIcon() = appContext().packageManager
            .getApplicationIcon(BuildConfig.APPLICATION_ID)
}