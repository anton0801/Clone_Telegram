package app.beer.telegram.ui.fragments.basesFragments

import androidx.fragment.app.Fragment
import app.beer.telegram.utils.APP_ACTIVITY

/**
 * A simple [Fragment] subclass.
 */
open class BaseFragment(layout: Int) : Fragment(layout) { // open это нужно чтобы можно было от этого класса можно было отнследоваться

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.appDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.appDrawer.enableDrawer()
    }

}
