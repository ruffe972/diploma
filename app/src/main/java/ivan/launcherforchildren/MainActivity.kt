package ivan.launcherforchildren

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var model: MainActivityModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = MainActivityModel(applicationContext)
        model?.let {
            view_pager.adapter = HomePagerAdapter(supportFragmentManager, it)
        }
    }
}
