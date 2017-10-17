package net.squanchy.imageloader

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.module.AppGlideModule
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StreamDownloadTask
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.security.MessageDigest

@GlideModule
internal class SquanchyGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(StorageReference::class.java, InputStream::class.java, FirebaseImageLoader.Factory())
    }
}

private class FirebaseImageLoader : ModelLoader<StorageReference, InputStream> {

    override fun buildLoadData(reference: StorageReference, width: Int, height: Int, options: Options?): ModelLoader.LoadData<InputStream> {
        return ModelLoader.LoadData(FirebaseStorageKey(reference), FirebaseStorageFetcher(reference))
    }

    override fun handles(model: StorageReference?) = model != null

    internal class Factory : ModelLoaderFactory<StorageReference, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<StorageReference, InputStream> {
            return FirebaseImageLoader()
        }

        override fun teardown() {
            // Nothing to do here, we don't own the FirebaseStorage nor the reference
        }
    }

    private inner class FirebaseStorageKey(private val reference: StorageReference) : Key {

        override fun updateDiskCacheKey(digest: MessageDigest) {
            digest.update(reference.path.byteInputStream(Charset.defaultCharset()).readBytes())
        }
    }

    private inner class FirebaseStorageFetcher internal constructor(private val reference: StorageReference) : DataFetcher<InputStream> {

        private lateinit var streamTask: StreamDownloadTask
        private var inputStream: InputStream? = null

        override fun loadData(priority: Priority?, callback: DataFetcher.DataCallback<in InputStream>?) {
            streamTask = reference.stream
            streamTask.apply {
                addOnSuccessListener {
                    inputStream = it.stream
                    callback?.onDataReady(inputStream)
                }
                addOnFailureListener { callback?.onLoadFailed(it) }
            }
        }

        // TODO consider this could be fetched from local cache too
        override fun getDataSource(): DataSource = DataSource.REMOTE

        override fun getDataClass(): Class<InputStream> = InputStream::class.java

        override fun cancel() {
            if (streamTask.isInProgress) {
                streamTask.cancel()
            }
        }

        override fun cleanup() {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                Timber.w("Could not close stream", e)
            }
        }
    }
}
