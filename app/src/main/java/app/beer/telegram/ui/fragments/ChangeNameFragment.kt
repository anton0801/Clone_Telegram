package app.beer.telegram.ui.fragments

import app.beer.telegram.MainActivity

import app.beer.telegram.R
import app.beer.telegram.ui.fragments.basesFragments.BaseChangeFragment
import app.beer.telegram.utils.*
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).appDrawer.disableDrawer()
        initFullnameList()
    }

    override fun change() {
        val name = settings_input_name.text.toString()
        val last_name = settings_input_last_name.text.toString()
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $last_name"
            REF_DATABASE_ROOT.child(NODE_USERS)
                .child(CURRENT_UID)
                .child(CHILD_FULL_NAME)
                .setValue(fullname)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast(getString(R.string.toast_data_updated))
                        USER.fullname = fullname
                        APP_ACTIVITY.appDrawer.updateHeader()
                        fragmentManager?.popBackStack()
                    } else {
                        showToast(getString(R.string.warning))
                    }
                }
        }
    }

    fun initFullnameList() {
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.size > 1) {
            settings_input_name.setText(fullnameList[0])
            settings_input_last_name.setText(fullnameList[1])
        } else {
            settings_input_name.setText(fullnameList[0])
        }
    }

}
