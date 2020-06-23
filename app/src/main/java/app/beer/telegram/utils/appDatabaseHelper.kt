package app.beer.telegram.utils

import android.net.Uri
import app.beer.telegram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

const val NODE_USERS = "users"
const val NODE_USERSNAMES = "usernames"

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULL_NAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var CURRENT_UID: String
lateinit var USER: User

lateinit var REF_STORAGE_ROOT: StorageReference

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = User()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) { // inline это значит что мы по сути не создаем функцию мы просьто выполняем код который там есть. Они не создаются и не выполняются они как к примеру ты взял и скопировал код и вставил туда куда тебе нужно вот это и inline функций
    // еще они нужны для больщей производительности
    REF_DATABASE_ROOT.child(NODE_USERS)
        .child(CURRENT_UID)
        .child(CHILD_PHOTO_URL)
        .setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putImageToStorage(
    uri: Uri?,
    path: StorageReference,
    crossinline function: () -> Unit
) { // function: () -> Unit эот lambda
    if (uri != null) {
        path.putFile(uri)
            .addOnSuccessListener { function() }
            .addOnFailureListener { showToast(it.message.toString()) }
    }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS)
        .child(CURRENT_UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(User::class.java) ?: User() // elvis оператор он выполнит первые строки ели не null и вторые строки если null
            if (USER.username.isEmpty()) {
                USER.username = CURRENT_UID
            }
            function()
        })
    // этот метод он сработает только один раз при запуске заполнит данные и все если будет указан этот метод addListenerForSingleValueEvent
    // а если addValueEventListener то он будет проверять если изменения в базе данных он будет постаянно обновлять те или иные данные
    // addChildEventListener он проверяет только один определёный child
}
