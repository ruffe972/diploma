package ivan.launcherforchildren

class MainActivityModel {
    val activityLists: List<List<ActivityInfo>>
    val dockActivities: List<ActivityInfo>
    val iconsInDock: Int

    init {
        val context = MainManager.context()
        val activities = MainManager.appsManager
                .allowedActivities()
                .filter { it.packageName != BuildConfig.APPLICATION_ID }
                .toMutableList()
        activities.add(ActivityInfo(
                "",
                BuildConfig.APPLICATION_ID,
                context.getString(R.string.parent_mode),
                MainManager.getDrawable(R.drawable.unlock_icon_round)
        ))
        val packagesInDock = listOf(
                "com.google.android.dialer",
                "com.android.dialer",
                "com.android.contacts",
                "com.google.android.apps.messaging",
                "com.android.messaging",
                "com.google.android.deskclock",
                "com.android.deskclock",
                BuildConfig.APPLICATION_ID
        )
        dockActivities = activities.filter {
            it.packageName in packagesInDock
        }
        iconsInDock = dockActivities.size
        activities.removeAll(dockActivities)
        activityLists = activities.chunked(MAX_APPS_PER_SCREEN)
    }

    companion object {
        const val APPS_COLUMNS_NUMBER = 4
        const val APPS_ROWS_NUMBER = 4
        const val MAX_APPS_PER_SCREEN = APPS_COLUMNS_NUMBER * APPS_ROWS_NUMBER
    }
}