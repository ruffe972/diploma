package ivan.launcherforchildren

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dock_app.view.*

class MainActivity : AppCompatActivity(), HomeAppGridFragment.InteractionListener {
    var model: MainActivityModel? = null

    override fun onAppClick(appInfo: MainActivityModel.AppInfo) {
        val intent = applicationContext.packageManager
                .getLaunchIntentForPackage(appInfo.packageName)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = MainActivityModel(applicationContext)
        val model = model ?: return
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
        val model = model ?: return
        for (appInfo in model.dockApps) {
            val iconView = layoutInflater.inflate(R.layout.dock_app, content, false)
                    .dock_app
            iconView.apply {
                setImageDrawable(appInfo.icon)
                setOnClickListener { onAppClick(appInfo) }
            }
            iconView.layoutParams = iconView.layoutParams.let {
                it.width = dock.width / MainActivityModel.APPS_IN_DOCK
                it
            }
            dock.addView(iconView)
        }
    }
}
