package ivan.launcherforchildren

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.fragment.app.Fragment
import ivan.launcherforchildren.MainActivityModel.AppInfo
import kotlinx.android.synthetic.main.home_grid_app.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TableLayout

private const val ARG_SCREEN_NUMBER = "screen_number"

// Single grid of apps on the home screen above the dock.
// Attach only to MainActivity.
class HomeAppGridFragment : Fragment() {
    private var appInfoList: List<AppInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screenNumber = arguments?.getInt(ARG_SCREEN_NUMBER) ?: 0
        appInfoList = (activity as MainActivity)
                .model
                ?.appInfoLists
                ?.get(screenNumber)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return TableLayout(activity).apply {
            layoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            )
        }
    }

    // view is fragment's view
    private fun fillGrid(view: ViewGroup) {
        val appInfoMatrix = (appInfoList ?: return)
                .chunked(MainActivityModel.APPS_COLUMNS_NUMBER)
        for (appInfoRow in appInfoMatrix) {
            val tableRow = TableRow(activity).apply {
                layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT)
            }
            for (appInfo in appInfoRow) {
                val appView = LayoutInflater.from(activity)
                        .inflate(R.layout.home_grid_app, tableRow, false)
                appView.linear_layout.apply {
                    app_name.text = appInfo.labelName
                    icon.setImageDrawable(appInfo.icon)
                }
                appView.layoutParams = appView.layoutParams.let {
                    it.height = view.measuredHeight / MainActivityModel.APPS_ROWS_NUMBER
                    it.width = view.width / MainActivityModel.APPS_COLUMNS_NUMBER
                    it
                }
                tableRow.addView(appView)
            }
            view.addView(tableRow)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewTreeObserver = view.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object: OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    fillGrid(view as ViewGroup)
                }
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(screenNumber: Int) =
                HomeAppGridFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SCREEN_NUMBER, screenNumber)
                    }
                }
    }
}

