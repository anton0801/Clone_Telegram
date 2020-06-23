package app.beer.telegram.ui.objects

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import app.beer.telegram.R
import app.beer.telegram.ui.fragments.SettingsFragment
import app.beer.telegram.utils.USER
import app.beer.telegram.utils.downloadAndSetImage
import app.beer.telegram.utils.replaceFragment
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDrawer(val mainActivity: AppCompatActivity, val toolbar: Toolbar) {

    private lateinit var drawer: Drawer // lateinit var это значит что мы обьявляем пересенные
    private lateinit var header: AccountHeader
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var currentProfile: ProfileDrawerItem

    fun create() {
        initLoader()
        createHeader()
        createDrawer()
        drawerLayout = drawer.drawerLayout
    }

    fun disableDrawer() {
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled =
            false // отключение gamburgerMenu и drawer
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED) // чтобы заблокировать в закрытом состояний
        toolbar.setNavigationOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer()
        }
    }

    private fun createDrawer() {
        drawer = DrawerBuilder()
            .withActivity(mainActivity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true) // гамбургер меню
            .withSelectedItem(-1) // по умолчанию меню
            .withAccountHeader(header)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(100)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_create_groups)
                    .withName("Создать группу")
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(101)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_secret_chat)
                    .withName("Создать секретный чат")
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(102)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_create_channel)
                    .withName("Создать каннал")
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(103)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_contacts)
                    .withName("контакты")
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(104)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_phone)
                    .withName("звонки")
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(105)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_favorites)
                    .withName("Избранное")
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(106)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_settings)
                    .withName("настройки")
                    .withSelectable(false),

                DividerDrawerItem(), // разделитель

                PrimaryDrawerItem().withIdentifier(107)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_invate)
                    .withName("пригласить друзей")
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(108)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_help)
                    .withName("Вопросы о Telegram")
                    .withSelectable(false)
            )
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position) {
                        7 -> mainActivity.replaceFragment(SettingsFragment())
                    }
                    return false
                }
            })
            .build()
    }

    private fun createHeader() {
        currentProfile = ProfileDrawerItem()
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
            .withIdentifier(200)

        header = AccountHeaderBuilder()
            .withActivity(mainActivity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(currentProfile)
            .build()
    }

    fun updateHeader() {
        currentProfile
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)

        header.updateProfile(currentProfile)
    }

    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.downloadAndSetImage(uri.toString())
            }
        })
    }


}