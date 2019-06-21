package ru.edu.masu.kids_shell

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dock_app.view.*

class MainActivity :
        AppCompatActivity(),
        HomeAppGridFragment.InteractionListener {
    lateinit var model: MainActivityModel
        private set
    private lateinit var adapter: HomePagerAdapter
    private val appsManager = MainManager.appsManager

    override fun onBackPressed() {
    }

    override fun onAppClick(activityInfo: ActivityInfo) {
        val componentName = ComponentName(activityInfo.packageName, activityInfo.name)
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            component = componentName
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        appsManager.lock()
        refresh()
    }

    private fun refresh() {
        model = MainActivityModel()
        dock.callOnceOnGlobalLayout(::refreshDock)
        dock.requestLayout()
        refreshViewPager()
    }

    private fun refreshViewPager() {
        if (!::adapter.isInitialized) {
            adapter = HomePagerAdapter(supportFragmentManager, model)
            view_pager.adapter = adapter
        } else {
            adapter.model = model
            adapter.notifyDataSetChanged()
        }
    }

    private fun refreshDock() {
        dock.removeAllViews()
        for (activityInfo in model.dockActivities) {
            val iconView = layoutInflater.inflate(R.layout.dock_app, content, false)
                    .dock_app
            iconView.setImageDrawable(activityInfo.icon)
            if (activityInfo.packageName == BuildConfig.APPLICATION_ID) {
                iconView.setOnClickListener {
                    val intent = Intent(this@MainActivity, PinLockActivity::class.java)
                    intent.putExtra(PinLockActivity.Mode.UNLOCK_PIN)
                    startActivity(intent)
                }
            } else {
                iconView.setOnClickListener { onAppClick(activityInfo) }
            }
            iconView.layoutParams = iconView.layoutParams.let {
                it.width = dock.width / model.iconsInDock
                it
            }
            dock.addView(iconView)
        }
    }
}
