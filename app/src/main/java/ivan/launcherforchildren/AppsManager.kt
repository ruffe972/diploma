package ivan.launcherforchildren

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

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

    private lateinit var allowedPackages: Set<String>
    private val allActivities: List<ActivityInfo>

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

    fun allowedActivities(): List<ActivityInfo> {
        return allActivities.filter {
            it.packageName in allowedPackages
        }
    }

    private fun save(context: Context) {
        context.openFileOutput(CONFIG_FILE_NAME, Context.MODE_PRIVATE).use { fileStream ->
            ObjectOutputStream(fileStream).use { objectStream ->
                objectStream.writeObject(allowedPackages)
            }
        }
    }

    private fun installedPackages() = allActivities.map { it.packageName }
            .toSet()

    private fun load(context: Context) {
        if (File(CONFIG_FILE_NAME).exists()) {
            context.openFileInput(CONFIG_FILE_NAME).use { fileStream ->
                ObjectInputStream(fileStream).use { objectStream ->
                    allowedPackages = (objectStream.readObject() as List<*>)
                            .map { it as String }
                            .toSet()
                            .intersect(installedPackages())
                }
            }
        } else {
            allowedPackages = installedPackages()
        }
    }
}