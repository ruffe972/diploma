package ivan.launcherforchildren

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

class AllAppsViewContent(context: Context) {

    val items: List<AllAppsViewItem>

    init {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val activities = pm.queryIntentActivities(intent, 0)

        fun activityToAllAppsViewItem(activity: ResolveInfo): AllAppsViewItem {
            val label = activity.loadLabel(pm).toString()
            val icon = activity.loadIcon(pm)
            return AllAppsViewItem(label, icon)
        }

        items = activities.map(::activityToAllAppsViewItem)
    }

    data class AllAppsViewItem(val appName: String, val icon: Drawable)
}