package ivan.launcherforchildren

import android.view.View
import android.view.ViewTreeObserver

fun View.callOnceOnGlobalLayout(callback: () -> Unit) {
    if (viewTreeObserver.isAlive) {
        viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                callback()
            }
        })
    }
}
