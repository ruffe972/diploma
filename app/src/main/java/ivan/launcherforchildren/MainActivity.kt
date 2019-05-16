package ivan.launcherforchildren

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
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
        model?.let { model ->
            view_pager.adapter = HomePagerAdapter(supportFragmentManager, model)
            initDock(model)
        }
    }

    private fun initDock(model: MainActivityModel) {
        val dockApps = model.dockApps
        val constraints = ConstraintSet()
        for ((i, appInfo) in dockApps.withIndex()) {
            val iconView = layoutInflater.inflate(R.layout.dock_app, content, false)
                    .dock_app
            iconView.apply {
                id = i
                setImageDrawable(appInfo.icon)
                setOnClickListener { onAppClick(appInfo) }
            }
            content.addView(iconView) // order?
            constraints.connect(i, ConstraintSet.TOP, R.id.dock, ConstraintSet.TOP)
            constraints.connect(i, ConstraintSet.BOTTOM, R.id.dock, ConstraintSet.BOTTOM)
            if (i > 0) {
                constraints.connect(i, ConstraintSet.LEFT, i - 1, ConstraintSet.RIGHT)
            }
            if (i < dockApps.size - 1) {
                constraints.connect(i, ConstraintSet.RIGHT, i + 1, ConstraintSet.LEFT) //i+1 not yet ready?
            }
        }
        constraints.apply {
            connect(0, ConstraintSet.LEFT, R.id.dock, ConstraintSet.LEFT)
            connect(dockApps.size - 1, ConstraintSet.RIGHT, R.id.dock, ConstraintSet.RIGHT)
            applyTo(content)
        }
    }
}
