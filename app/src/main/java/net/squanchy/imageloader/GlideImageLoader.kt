package net.squanchy.imageloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GlideImageLoader(private val requestManager: RequestManager, private val firebaseStorage: FirebaseStorage) : ImageLoader {

    companion object {
        val FIREBASE_URL_SCHEMA = "gs://"
    }

    override fun load(url: String): ImageRequest {
        return if (url.startsWith(FIREBASE_URL_SCHEMA))
            load(firebaseStorage.getReferenceFromUrl(url))
        else
            GlideImageRequest(requestManager.load(url))
    }

    override fun load(storageReference: StorageReference): ImageRequest = GlideImageRequest(requestManager.load(storageReference))
}

interface ImageRequest {

    fun into(target: ImageView)
}

private class GlideImageRequest(private val request: RequestBuilder<Drawable>) : ImageRequest {

    override fun into(target: ImageView) {
        request.into(target)
    }
}
