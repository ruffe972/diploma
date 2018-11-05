package ivan.launcherforchildren

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ivan.launcherforchildren.AllAppsFragment.OnFragmentInteractionListener

class AllAppsAdapter(private val values: List<AllAppsViewContent.Item>,
                     private val listener: OnFragmentInteractionListener?): BaseAdapter() {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val appInfo = values[position]
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.all_apps_item, parent, false)
        with(view) {
            findViewById<TextView>(R.id.app_name).text = appInfo.labelName
            findViewById<ImageView>(R.id.icon).setImageDrawable(appInfo.icon)
            setOnClickListener { listener?.onAllAppsFragmentInteraction(appInfo) }
        }
        return view
    }

    override fun getItem(position: Int): Any = Unit

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = values.size
}