package ivan.launcherforchildren

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ivan.launcherforchildren.dummy.DummyContent

class MainActivity : AppCompatActivity(), ItemFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
    }
}
