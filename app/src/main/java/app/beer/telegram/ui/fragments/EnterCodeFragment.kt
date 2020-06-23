package app.beer.telegram.ui.fragments

import androidx.fragment.app.Fragment
import app.beer.telegram.MainActivity

import app.beer.telegram.R
import app.beer.telegram.activities.RegisterActivity
import app.beer.telegram.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*

/**
 * A simple [Fragment] subclass.
 */
class EnterCodeFragment(val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = phoneNumber
        register_input_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_code.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) { // it = task
                val dateMap = mutableMapOf<String, Any>() // первое ключ, и значение
                val uid = AUTH.currentUser?.uid.toString()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber
                dateMap[CHILD_USERNAME] = uid // изначальное имя

                REF_DATABASE_ROOT.child(NODE_USERS)
                    .child(uid)
                    .updateChildren(dateMap)
                    .addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            showToast("Добро пожаловать!")
                            (activity as RegisterActivity).replaceActivity(MainActivity())
                        } else {
                            showToast(task2.exception?.message.toString())
                        }
                    }
            } else showToast(it.exception?.message.toString())
        }
    }

}
