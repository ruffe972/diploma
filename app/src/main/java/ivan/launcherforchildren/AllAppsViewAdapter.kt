package ivan.launcherforchildren

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ivan.launcherforchildren.AllAppsFragment.OnFragmentInteractionListener
import kotlinx.android.synthetic.main.all_apps_item.view.*

class AllAppsViewAdapter(
        private val values: List<AllAppsViewContent.Item>,
        private val listener: OnFragmentInteractionListener?)
    : RecyclerView.Adapter<AllAppsViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as AllAppsViewContent.Item
            // Notify the activity that an item has been selected.
            listener?.onAllAppsFragmentInteraction(item)
        }
    }

    override fun getItemCount(): Int = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.all_apps_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.appNameView.text = item.labelName
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