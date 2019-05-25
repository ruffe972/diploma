package ivan.launcherforchildren

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import java.io.File
import java.io.OutputStreamWriter

data class ActivityInfo(
        val name: String,
        val packageName: String,
        val labelName: String,
        val icon: Drawable
)

class AppsManager(context: Context) {

    companion object {
        const val CONFIG_FILE_NAME = "app_manager"
    }

    private lateinit var allowedPackages: MutableSet<String>
    val allActivities: List<ActivityInfo>

    init {
        val intent = Intent(Intent.ACTION_MAIN, null)
                .addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = context.packageManager
        allActivities = pm.queryIntentActivities(intent, 0)
                .map {
                    ActivityInfo(
                            it.activityInfo.name,
                            it.activityInfo.packageName,
                            it.loadLabel(pm).toString(),
                            it.loadIcon(pm)
                    )
                }
        load(context)
    }

    fun allowPackage(packageName: String) = allowedPackages.add(packageName)

    fun blockPackage(packageName: String) = allowedPackages.remove(packageName)

    fun isAllowed(packageName: String) = packageName in allowedPackages

    fun allowedActivities(): List<ActivityInfo> {
        return allActivities.filter {
            it.packageName in allowedPackages
        }
    }

    fun save(context: Context) {
        context.openFileOutput(CONFIG_FILE_NAME, Context.MODE_PRIVATE).use { fileStream ->
            OutputStreamWriter(fileStream).use { writer ->
                allowedPackages.forEach {
                    writer.write(it + '\n')
                }
            }
        }
    }

    private fun installedPackages() = allActivities.map { it.packageName }
            .toSet()

    private fun load(context: Context) {
        val file = File(context.filesDir, CONFIG_FILE_NAME)
        allowedPackages = if (file.exists()) {
            file.readLines()
                    .toSet()
                    .intersect(installedPackages())
                    .toMutableSet()
        } else {
            installedPackages().toMutableSet()
        }
    }
}