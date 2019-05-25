package ivan.launcherforchildren

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.blacklist_item.view.*

class BlacklistAdapter(
        private val activities: List<ActivityInfo>,
        private val listener: BlacklistInteractionListener
): RecyclerView.Adapter<BlacklistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.blacklist_item, parent, false)
        return ViewHolder(view, view.icon, view.app_name, view.checkbox)
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activityInfo = activities[position]
        holder.apply {
            icon.setImageDrawable(activityInfo.icon)
            textView.text = activityInfo.labelName
            checkbox.isChecked = MainManager.appsManager
                    .isAllowed(activityInfo.packageName)
            checkbox.setOnClickListener { listener.onCheckboxClick(checkbox, activityInfo) }
        }
    }

    class ViewHolder(
            itemView: View,
            val icon: ImageView,
            val textView: TextView,
            val checkbox: CheckBox
    ) : RecyclerView.ViewHolder(itemView)
}