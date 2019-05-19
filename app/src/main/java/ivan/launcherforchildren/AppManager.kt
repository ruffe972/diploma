package ivan.launcherforchildren

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

data class AppInfo(
        val packageName: String,
        val labelName: String,
        val icon: Drawable)

// The manager of installed apps
class AppManager(appContext: Context) {
    val allApps: List<AppInfo>

    private fun resolveInfoToAppInfo(
            packageManager: PackageManager,
            resolveInfo: ResolveInfo): AppInfo {
        return AppInfo(
                resolveInfo.activityInfo.packageName,
                resolveInfo.loadLabel(packageManager).toString(),
                resolveInfo.loadIcon(packageManager))
    }

    init {
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val packageManager = appContext.packageManager
        allApps = packageManager.queryIntentActivities(intent, 0)
                .map { resolveInfoToAppInfo(packageManager, it) }
    }
}