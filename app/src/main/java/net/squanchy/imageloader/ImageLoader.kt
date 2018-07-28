package net.squanchy.imageloader

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.google.firebase.storage.StorageReference

interface ImageLoader {

    fun load(url: String): ImageRequest

    fun load(storageReference: StorageReference): ImageRequest
}

interface ImageRequest {

    fun error(@DrawableRes errorImageResId: Int): ImageRequest

    fun placeholder(@DrawableRes placeholderImageResId: Int): ImageRequest

    fun into(target: ImageView)
}
