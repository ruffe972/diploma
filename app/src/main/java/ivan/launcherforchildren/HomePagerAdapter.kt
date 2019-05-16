package ivan.launcherforchildren

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HomePagerAdapter(
        fm: FragmentManager,
        private val model: MainActivityModel): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) = HomeAppGridFragment.newInstance(position)

    override fun getCount(): Int = model.appInfoLists.count()
}