package ivan.launcherforchildren

class MainActivityModel {
    val appInfoLists: List<List<AppInfo>>
    val dockApps: List<AppInfo>

    init {
        val allApps = MainManager.appManager.allApps
        dockApps = allApps.slice(0 until APPS_IN_DOCK)
        appInfoLists = allApps.slice(APPS_IN_DOCK until allApps.size)
                .chunked(MAX_APPS_PER_SCREEN)
    }

    companion object {
        const val APPS_COLUMNS_NUMBER = 4
        const val APPS_ROWS_NUMBER = 5
        const val MAX_APPS_PER_SCREEN = APPS_COLUMNS_NUMBER * APPS_ROWS_NUMBER
        const val APPS_IN_DOCK = 5
    }
}