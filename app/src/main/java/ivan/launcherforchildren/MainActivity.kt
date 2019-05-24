package ivan.launcherforchildren

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dock_app.view.*

class MainActivity : AppCompatActivity(), HomeAppGridFragment.InteractionListener {
    val model = MainActivityModel()

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
        view_pager.adapter = HomePagerAdapter(supportFragmentManager, model)

        val viewTreeObserver = dock.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    dock.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    initDock()
                }
            })
        }
    }

    private fun initDock() {
        for (activityInfo in model.dockActivities) {
            val iconView = layoutInflater.inflate(R.layout.dock_app, content, false)
                    .dock_app
            iconView.setImageDrawable(activityInfo.icon)
            if (activityInfo.packageName == BuildConfig.APPLICATION_ID) {
                iconView.setOnClickListener {
                    val intent = Intent(this, AppBlacklistActivity::class.java)
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
