package ru.edu.masu.kids_shell

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import java.io.File
import java.io.OutputStreamWriter
import java.util.*
import kotlin.concurrent.fixedRateTimer

data class ActivityInfo(
        val name: String,
        val packageName: String,
        val labelName: String,
        val icon: Drawable
)

class AppsManager(private val context: Context) {

    companion object {
        private const val CONFIG_FILE_NAME = "app_manager"
        private const val USAGE_CHECK_INTERVAL = 1000L
    }

    // All activites with CATEGORY_LAUNCHER
    val allLauncherActivities: List<ActivityInfo>

    private var allowedPackages: MutableSet<String> = mutableSetOf()
    private val usageStatsManager: UsageStatsManager
    private var timer: Timer? = null

    init {
        val intent = Intent(Intent.ACTION_MAIN, null)
                .addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = context.packageManager
        allLauncherActivities = pm.queryIntentActivities(intent, 0)
                .map {
                    ActivityInfo(
                            it.activityInfo.name,
                            it.activityInfo.packageName,
                            it.loadLabel(pm).toString(),
                            it.loadIcon(pm)
                    )
                }
        load()

        @SuppressLint("WrongConstant")
        usageStatsManager = context.getSystemService("usagestats") as UsageStatsManager
    }

    fun allowPackage(packageName: String) = allowedPackages.add(packageName)

    fun blockPackage(packageName: String) = allowedPackages.remove(packageName)

    fun isAllowed(packageName: String) = packageName in allowedPackages

    fun allowedActivities(): List<ActivityInfo> {
        return allLauncherActivities.filter {
            it.packageName in allowedPackages
        }
    }

    fun save() {
        context.openFileOutput(CONFIG_FILE_NAME, Context.MODE_PRIVATE).use { fileStream ->
            OutputStreamWriter(fileStream).use { writer ->
                allowedPackages.forEach {
                    writer.write(it + '\n')
                }
            }
        }
    }

    fun lock() {
        if (timer != null) {
            return
        }
        timer = fixedRateTimer(initialDelay = USAGE_CHECK_INTERVAL, period = USAGE_CHECK_INTERVAL) {
            if (appInForeground() !in allowedPackages) {
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                }
                context.startActivity(intent)
            }
        }
    }

    fun unlock() {
        timer?.cancel()
        timer = null
    }

    private fun appInForeground(): String {
        val currentTime = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                currentTime - 10000,
                currentTime
        )  // TODO dangerous at midnight
        return stats.maxBy { it.lastTimeUsed }
                ?.packageName ?: BuildConfig.APPLICATION_ID
    }

    private fun installedPackagesWithLauncherIcon() = allLauncherActivities.map { it.packageName }
            .toSet()

    private fun load() {
        val file = File(context.filesDir, CONFIG_FILE_NAME)
        allowedPackages = if (file.exists()) {
            file.readLines()
                    .toSet()
                    .intersect(installedPackagesWithLauncherIcon())
        } else {
            installedPackagesWithLauncherIcon().filter {
                it !in setOf(
                        "com.google.android.apps.nexuslauncher",
                        "com.android.launcher3"
                )  // TODO remove all launchers
            }
        }.toMutableSet()
    }

}