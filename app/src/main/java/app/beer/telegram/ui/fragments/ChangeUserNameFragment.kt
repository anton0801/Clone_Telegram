package app.beer.telegram.ui.fragments

import app.beer.telegram.MainActivity

import app.beer.telegram.R
import app.beer.telegram.ui.fragments.basesFragments.BaseChangeFragment
import app.beer.telegram.utils.*
import kotlinx.android.synthetic.main.fragment_change_user_name.*

class ChangeUserNameFragment : BaseChangeFragment(R.layout.fragment_change_user_name) {

    lateinit var newUserName: String
    private var isOk = false

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).appDrawer.disableDrawer()
//        settings_input_username.addTextChangedListener(AppTextWatcher() {
//            if (settings_input_username.text.length < 5) {
//                error_username.visibility = View.VISIBLE
//                error_username.text =
//                    String.format(getString(R.string.username_warning_lenght), newUserName.length)
//            } else {
//                isOk = true
//                error_username.visibility = View.GONE
//                error_username.text = ""
//            }
//        })
    }

    override fun change() {
        newUserName = settings_input_username.text.toString()//.toLowerCase(Locale.getDefault())
        if (newUserName.isEmpty()) {
            showToast(getString(R.string.username_must_been_no_empty))
        } else if (newUserName.length >= 5) {
            REF_DATABASE_ROOT.child(NODE_USERSNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener() {
                    if (it.hasChild(newUserName)) {
                        showToast(getString(R.string.user_exists))
                    } else {
                        changeUsername()
                        APP_ACTIVITY.appDrawer.updateHeader()
                    }
                })
        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERSNAMES)
            .child(newUserName)
            .setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUserName()
                }
            }
    }

    private fun updateCurrentUserName() {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .setValue(newUserName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    //showToast(getString(R.string.toast_data_updated))
                    deleteOldUsername()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERSNAMES)
            .child(USER.username)
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.toast_data_updated))
                    fragmentManager?.popBackStack()
                    USER.username = newUserName
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

}
