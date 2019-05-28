package ru.edu.masu.kids_shell

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class HomePagerAdapter(
        fm: FragmentManager,
        var model: MainActivityModel
): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = HomeAppGridFragment.newInstance(position)

    override fun getCount(): Int = model.activityLists.count()

    override fun getItemPosition(`object`: Any) = POSITION_NONE
}