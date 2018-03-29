package net.squanchy.settings.view

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_settings.view.userCirclePhotoView
import kotlinx.android.synthetic.main.activity_settings.view.usernameTextView
import net.squanchy.R
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.imageLoaderComponent
import net.squanchy.service.repository.GoogleData
import net.squanchy.service.repository.User
import net.squanchy.support.lang.Optional
import net.squanchy.support.unwrapToActivityContext

class SettingsHeaderLayout(context: Context, attrs: AttributeSet?) : AppBarLayout(context, attrs) {

    private var imageLoader: ImageLoader? = null

    init {
        if (!isInEditMode) {
            imageLoader = imageLoaderComponent(unwrapToActivityContext(context))
                .imageLoader()
        }

        super.setOrientation(LinearLayout.VERTICAL)
    }

    fun updateWith(user: Optional<User>) {
        if (user.isPresent && !user.get().isAnonymous) {
            updateWithAuthenticatedUser(user.get())
        } else {
            updateWithNoOrAnonymousUser()
        }
    }

    private fun updateWithAuthenticatedUser(user: User) {
        val googleUserInfo = user.googleData
        if (googleUserInfo != null) {
            updateUserPhotoFrom(googleUserInfo)
            usernameTextView.text = googleUserInfo.displayName
        }
    }

    private fun updateUserPhotoFrom(userInfo: GoogleData) {
        if (imageLoader == null) {
            return
        }

        if (userInfo.photoUrl != null) {
            imageLoader!!.load(userInfo.photoUrl)
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
