package ivan.launcherforchildren

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), AllAppsFragment.OnAllAppsFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onAllAppsFragmentInteraction(
            allAppsViewItem: AllAppsViewContent.AllAppsViewItem?) {
    }
}
