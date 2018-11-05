package ivan.launcherforchildren

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

class AllAppsViewContent(context: Context) {

    val items: List<Item>

    init {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfos = packageManager.queryIntentActivities(intent, 0)

        fun activityToAllAppsViewItem(resolveInfo: ResolveInfo): Item {
            return Item(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.loadIcon(packageManager))
        }

        items = resolveInfos.map(::activityToAllAppsViewItem)
    }

    data class Item(val packageName: String, val labelName: String, val icon: Drawable)
}