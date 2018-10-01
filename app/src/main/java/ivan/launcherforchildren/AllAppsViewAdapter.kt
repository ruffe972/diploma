package ivan.launcherforchildren

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ivan.launcherforchildren.AllAppsFragment.OnAllAppsFragmentInteractionListener
import ivan.launcherforchildren.AllAppsViewContent.AllAppsViewItem
import kotlinx.android.synthetic.main.fragment_all_apps_item.view.*

class AllAppsViewAdapter(
        private val values: List<AllAppsViewItem>,
        private val listener: OnAllAppsFragmentInteractionListener?)
    : RecyclerView.Adapter<AllAppsViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as AllAppsViewItem
            // Notify the activity that an item has been selected.
            listener?.onAllAppsFragmentInteraction(item)
        }
    }

    override fun getItemCount(): Int = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.fragment_all_apps_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.appNameView.text = item.appName
        holder.iconView.setImageDrawable(item.icon)

        with(holder.view) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val iconView: ImageView = view.icon
        val appNameView: TextView = view.app_name

        override fun toString(): String {
            return super.toString() + " '" + appNameView.text + "'"
        }
    }
}