package ivan.launcherforchildren

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

class MainActivityModel(context: Context) {

    val appInfoLists: List<List<AppInfo>>
    val dockApps: List<AppInfo>

    init {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfoList = packageManager.queryIntentActivities(intent, 0)

        fun resolveInfoToAppInfo(resolveInfo: ResolveInfo): AppInfo {
            return AppInfo(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.loadIcon(packageManager))
        }

        val allApps = resolveInfoList.map(::resolveInfoToAppInfo)
        val appsInDock = 5
        dockApps = allApps.slice(0 until appsInDock)
        appInfoLists = allApps.slice(appsInDock until allApps.size)
                .chunked(MAX_APPS_PER_SCREEN)
    }

    data class AppInfo(
            val packageName: String,
            val labelName: String,
            val icon: Drawable)

    companion object {
        const val APPS_COLUMNS_NUMBER = 4
        const val APPS_ROWS_NUMBER = 5
        const val MAX_APPS_PER_SCREEN = APPS_COLUMNS_NUMBER * APPS_ROWS_NUMBER
    }
}