package app.beer.telegram.ui.fragments

import androidx.fragment.app.Fragment
import app.beer.telegram.MainActivity

import app.beer.telegram.R
import app.beer.telegram.activities.RegisterActivity
import app.beer.telegram.utils.AUTH
import app.beer.telegram.utils.replaceActivity
import app.beer.telegram.utils.replaceFragment
import app.beer.telegram.utils.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var phoneNumber: String
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // credential он позволяет произвести авторизацию или регестрацию
                AUTH.signInWithCredential(credential).addOnCompleteListener{
                    if (it.isSuccessful) { // it = task
                        showToast("Добро пожаловать!")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    } else showToast(it.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)
                replaceFragment(EnterCodeFragment(phoneNumber, id))
            }

        }
        register_btn_next.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (register_input_phone_number.text.toString().isEmpty()) {
            showToast(getString(R.string.register_enter_phone_number_toast))
        } else {
            // replaceFragment(EnterCodeFragment())
            authUser()
        }
    }

    private fun authUser() {
        phoneNumber = register_input_phone_number.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            activity as RegisterActivity,
            callback
        )
    }

}
