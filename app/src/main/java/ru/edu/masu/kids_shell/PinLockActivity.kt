package ru.edu.masu.kids_shell

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.andrognito.pinlockview.PinLockListener
import kotlinx.android.synthetic.main.activity_pin_lock.*

class ChangePinStrategy(
        private val activity: PinLockActivity,
        private val initialText: String
) : PinLockActivity.Strategy {

    private var firstPin = ""

    override fun initialText(): String = initialText

    override fun onEmpty() = Unit

    override fun onPinChange(pinLength: Int, intermediatePin: String) = Unit

    override fun onComplete(pin: String) {
        when {
            firstPin.isEmpty() -> {
                firstPin = pin
                activity.apply {
                    text.text = getString(R.string.repeat_pin)
                    pin_lock_view.resetPinLockView()
                }
            }
            pin == firstPin -> saveNewPinAndExit()
            else -> activity.apply {
                text.text = getString(R.string.pins_do_not_match)
                pin_lock_view.resetPinLockView()
            }
        }
    }

    private fun saveNewPinAndExit() {
        val sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        )
        with (sharedPref.edit()) {
            putString(activity.getString(R.string.pin_prefs_key), firstPin)
            apply()
        }
        // TODO firstPin reset - activity lifecycle?
        activity.apply {
            Toast.makeText(
                    this,
                    getString(R.string.pw_change_success),
                    Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }
}

class UnlockPinStrategy(private val activity: PinLockActivity) : PinLockActivity.Strategy {

    override fun initialText(): String = activity.getString(R.string.enter_pin)

    override fun onEmpty() = Unit

    override fun onPinChange(pinLength: Int, intermediatePin: String) = Unit

    override fun onComplete(pin: String) {
        val sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        )
        val correctPin = sharedPref.getString(
                activity.getString(R.string.pin_prefs_key),
                ""
        )
        if (pin == correctPin) {
            val intent = Intent(activity, BlacklistActivity::class.java)
            activity.startActivity(intent)
        } else {
            activity.apply {
                text.text = getString(R.string.wrong_pin)
                pin_lock_view.resetPinLockView()
            }
        }
    }
}

class PinLockActivity : AppCompatActivity() {

    enum class Mode {
        INITIAL_PIN_SET,
        CHANGE_EXISTING_PIN,
        UNLOCK_PIN
    }

    companion object {
        const val PIN_LENGTH = 6
    }

    interface Strategy : PinLockListener {
        fun initialText(): String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_lock)
        val strategy = strategy()
        text.text = strategy.initialText()
        pin_lock_view.apply {
            setPinLockListener(strategy)
            attachIndicatorDots(indicator_dots)
            pinLength = PIN_LENGTH
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun strategy(): Strategy {
        return when (intent.getEnumExtra<Mode>()) {
            Mode.INITIAL_PIN_SET -> ChangePinStrategy(this, getString(R.string.initial_pin_text))
            Mode.CHANGE_EXISTING_PIN -> ChangePinStrategy(this, getString(R.string.enter_new_pin))
            else -> UnlockPinStrategy(this)
        }
    }
    
}
