package net.squanchy.imageloader

import com.google.firebase.storage.StorageReference

interface ImageLoader {

    fun load(url: String): ImageRequest

    fun load(storageReference: StorageReference): ImageRequest
}
