package app.beer.telegram

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import app.beer.telegram.activities.RegisterActivity
import app.beer.telegram.databinding.ActivityMainBinding
import app.beer.telegram.models.User
import app.beer.telegram.ui.fragments.ChatsFragment
import app.beer.telegram.ui.objects.AppDrawer
import app.beer.telegram.utils.*
import com.theartofdev.edmodo.cropper.CropImage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var appDrawer: AppDrawer
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser {
            initFields()
            initFunc()
        }
    }

    private fun initFields() {
        toolbar = binding.mainToolbar // это мы находим элементы
        appDrawer = AppDrawer(this, toolbar)
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(toolbar)
            appDrawer.create()
            replaceFragment(ChatsFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

}
