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
        val resolveInfos = pm.queryIntentActivities(intent, 0)

        fun activityToAllAppsViewItem(resolveInfo: ResolveInfo): AllAppsViewItem {
            return AllAppsViewItem(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.loadLabel(pm).toString(),
                    resolveInfo.loadIcon(pm))
        }

        items = resolveInfos.map(::activityToAllAppsViewItem)
    }

    data class AllAppsViewItem(val packageName: String, val labelName: String, val icon: Drawable)
}