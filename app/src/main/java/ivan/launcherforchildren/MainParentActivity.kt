package ivan.launcherforchildren

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainParentActivity : AppCompatActivity() {

    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_parent)
    }
}
