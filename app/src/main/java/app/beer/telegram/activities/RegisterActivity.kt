package app.beer.telegram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import app.beer.telegram.R
import app.beer.telegram.databinding.ActivityRegisterBinding
import app.beer.telegram.ui.fragments.EnterPhoneNumberFragment
import app.beer.telegram.utils.initFirebase
import app.beer.telegram.utils.replaceFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        toolbar = binding.registerToolbar
        setSupportActionBar(toolbar)
        title = getString(R.string.register_title_your_phone)
        replaceFragment(EnterPhoneNumberFragment(), false)
    }

}
