package net.squanchy.settings.view

import android.content.Context
import android.net.Uri
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import kotlinx.android.synthetic.main.activity_settings.view.userCirclePhotoView
import kotlinx.android.synthetic.main.activity_settings.view.usernameTextView
import net.squanchy.R
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.ImageLoaderInjector
import net.squanchy.support.lang.Lists
import net.squanchy.support.lang.Optional
import net.squanchy.support.unwrapToActivityContext

class SettingsHeaderLayout(context: Context, attrs: AttributeSet?) : AppBarLayout(context, attrs) {

    private var imageLoader: ImageLoader? = null

    init {

        if (!isInEditMode) {
            imageLoader = ImageLoaderInjector.obtain(unwrapToActivityContext(context))
                .imageLoader()
        }

        super.setOrientation(LinearLayout.VERTICAL)
    }

    fun updateWith(user: Optional<FirebaseUser>) {
        if (user.isPresent && !user.get().isAnonymous) {
            updateWithAuthenticatedUser(user.get())
        } else {
            updateWithNoOrAnonymousUser()
        }
    }

    private fun updateWithAuthenticatedUser(firebaseUser: FirebaseUser) {
        val googleUserInfo = googleUserInfoFrom(firebaseUser)
        if (googleUserInfo.isPresent) {
            val userInfo = googleUserInfo.get()
            updateUserPhotoFrom(userInfo)
            usernameTextView.text = userInfo.displayName
        }
    }

    private fun googleUserInfoFrom(firebaseUser: FirebaseUser): Optional<UserInfo> {
        val providerData = firebaseUser.providerData
        val googleData = Lists.filter(providerData) { data -> PROVIDER_ID_GOOGLE.equals(data.providerId, ignoreCase = true) }
        return if (googleData.isEmpty()) {
            Optional.absent()
        } else Optional.of(googleData[0])
    }

    private fun updateUserPhotoFrom(userInfo: UserInfo) {
        if (imageLoader == null) {
            return
        }

        val photoUrl = photoUrlFor(userInfo)
        if (photoUrl.isPresent) {
            imageLoader!!.load(photoUrl.get())
                .error(R.drawable.ic_no_avatar)
                .into(userCirclePhotoView)
        } else {
            userCirclePhotoView.setImageResource(R.drawable.ic_no_avatar)
        }
    }

    private fun photoUrlFor(userInfo: UserInfo): Optional<String> {
        return Optional.fromNullable<Uri>(userInfo.photoUrl)
            .map { it.toString() }
    }

    private fun updateWithNoOrAnonymousUser() {
        userCirclePhotoView.setImageResource(R.drawable.avatar_not_signed_in)
        usernameTextView.setText(R.string.settings_header_not_signed_in)
    }

    companion object {

        private const val PROVIDER_ID_GOOGLE = "google.com"
    }
}
