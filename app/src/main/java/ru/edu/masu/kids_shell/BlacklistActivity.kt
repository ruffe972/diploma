package ru.edu.masu.kids_shell

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_blacklist.*

// For recyclerView
interface BlacklistInteractionListener {
    fun onCheckboxClick(checkBox: CheckBox, activityInfo: ActivityInfo)
}

class BlacklistActivity : AppCompatActivity(), BlacklistInteractionListener {
    private val appsManager = MainManager.appsManager

    override fun onCheckboxClick(checkBox: CheckBox, activityInfo: ActivityInfo) {
        val name = activityInfo.packageName
        if (checkBox.isChecked) {
            appsManager.allowPackage(name)
        } else {
            appsManager.blockPackage(name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blacklist)
        appsManager.unlock()
        val activities = appsManager
                .allLauncherActivities
                .filter { it.packageName != BuildConfig.APPLICATION_ID }
        app_blacklist.setHasFixedSize(true)  // Improves performance
        app_blacklist.adapter = BlacklistAdapter(appsManager, activities, this)
        app_blacklist.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.parents_mode_menu, menu)
        return true
    }

    override fun onStop() {
        super.onStop()
        appsManager.save()
    }

    @Suppress("UNUSED_PARAMETER")
    fun changePin(item: MenuItem) {
        val intent = Intent(this, PinLockActivity::class.java)
        intent.putExtra(PinLockActivity.Mode.CHANGE_EXISTING_PIN)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun tryGoingToChildMode(item: MenuItem) {
        val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        )
        if (sharedPref.contains(getString(R.string.pin_prefs_key))) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
        } else {
            val intent = Intent(this, PinLockActivity::class.java)
            intent.putExtra(PinLockActivity.Mode.INITIAL_PIN_SET)
            startActivity(intent)
        }
    }
}
