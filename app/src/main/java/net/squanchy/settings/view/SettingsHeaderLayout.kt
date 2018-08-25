package net.squanchy.settings.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import arrow.core.Option
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_settings.view.*
import net.squanchy.R
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.imageLoaderComponent
import net.squanchy.service.repository.GoogleData
import net.squanchy.service.repository.User
import net.squanchy.support.lang.getOrThrow
import net.squanchy.support.unwrapToActivityContext

class SettingsHeaderLayout(context: Context, attrs: AttributeSet?) : AppBarLayout(context, attrs) {

    private var imageLoader: ImageLoader? = null

    init {
        if (!isInEditMode) {
            imageLoader = imageLoaderComponent(context.unwrapToActivityContext())
                .imageLoader()
        }

        super.setOrientation(LinearLayout.VERTICAL)
    }

    fun updateWith(user: Option<User>) {
        if (user.isDefined() && !user.getOrThrow().isAnonymous) {
            updateWithAuthenticatedUser(user.getOrThrow())
        } else {
            updateWithNoOrAnonymousUser()
        }
    }

    private fun updateWithAuthenticatedUser(user: User) {
        val googleUserInfo = user.googleData
        if (googleUserInfo != null) {
            updateUserPhotoFrom(googleUserInfo, imageLoader)
            usernameTextView.text = googleUserInfo.displayName
        }
    }

    private fun updateUserPhotoFrom(userInfo: GoogleData, imageLoader: ImageLoader?) {
        if (imageLoader == null) {
            throw IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet")
        }

        if (userInfo.photoUrl != null) {
            imageLoader.load(userInfo.photoUrl)
                .error(R.drawable.ic_no_avatar)
                .into(userCirclePhotoView)
        } else {
            userCirclePhotoView.setImageResource(R.drawable.ic_no_avatar)
        }
    }

    private fun updateWithNoOrAnonymousUser() {
        userCirclePhotoView.setImageResource(R.drawable.avatar_not_signed_in)
        usernameTextView.setText(R.string.settings_header_not_signed_in)
    }
}
