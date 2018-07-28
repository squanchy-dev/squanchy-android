package net.squanchy.imageloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GlideImageLoader(private val requestManager: RequestManager, private val firebaseStorage: FirebaseStorage) : ImageLoader {

    companion object {
        const val FIREBASE_URL_SCHEMA = "gs://"
    }

    override fun load(url: String): ImageRequest {
        return if (url.startsWith(FIREBASE_URL_SCHEMA)) {
            load(firebaseStorage.getReferenceFromUrl(url))
        } else {
            GlideImageRequest(requestManager.load(url))
        }
    }

    override fun load(storageReference: StorageReference): ImageRequest = GlideImageRequest(requestManager.load(storageReference))
}

private class GlideImageRequest(private val request: RequestBuilder<Drawable>) : ImageRequest {

    override fun error(@DrawableRes errorImageResId: Int) = apply {
        request.apply(RequestOptions.errorOf(errorImageResId))
    }

    override fun placeholder(@DrawableRes placeholderImageResId: Int) = apply {
        request.apply(RequestOptions.placeholderOf(placeholderImageResId))
    }

    override fun into(target: ImageView) {
        request.into(target)
    }
}
