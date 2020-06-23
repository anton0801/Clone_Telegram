package app.beer.telegram.ui.fragments

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import app.beer.telegram.R
import app.beer.telegram.activities.RegisterActivity
import app.beer.telegram.ui.fragments.basesFragments.BaseFragment
import app.beer.telegram.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true) // это нужно чтобы включить меню
        initField()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        when (id) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                APP_ACTIVITY.replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_edit_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    private fun initField() {
        if (USER.fullname.isEmpty()) {
            settings_full_name.text = USER.phone
        } else {
            settings_full_name.text = USER.fullname
        }

        if (USER.bio.isEmpty()) {
            settings_bio.text = "О себе"
        } else {
            settings_bio.text = USER.bio
        }

        if (USER.photoUrl.isNotEmpty()) {
            settings_user_photo.downloadAndSetImage(USER.photoUrl)
        } else {
            settings_user_photo.setImageResource(R.drawable.default_photo)
        }

        settings_phone_number.text = USER.phone
        settings_status.text = USER.state
        settings_username.text = "@" + USER.username

        settings_btn_change_username.setOnClickListener {
            replaceFragment(ChangeUserNameFragment())
        }

        settings_btn_change_bio.setOnClickListener {
            replaceFragment(ChangeBioFragment())
        }

        settings_change_photo.setOnClickListener { changePhotoUser() }
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(
                1,
                1
            ) // это чтобы Croper был пропорцианалень, чтобы не растягивался никак
            .setRequestedSize(
                600,
                600
            ) // если картика боль 600 px то он его обрежет если нет то оставит так как есть. Это нужно чтобы картинка весила меньше
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
            resultCode == Activity.RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)


            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDatabase(it) {
                        settings_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_updated))
                        USER.photoUrl = it
                        APP_ACTIVITY.appDrawer.updateHeader()
                    }
                }
            }
        }
    }

    fun checkStatus() {

    }

}
