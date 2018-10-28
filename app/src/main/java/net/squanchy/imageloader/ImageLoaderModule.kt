package net.squanchy.imageloader

import android.app.Activity
import com.bumptech.glide.RequestManager
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
internal class ImageLoaderModule {

    @Provides
    fun glideRequestManager(activity: Activity): RequestManager = GlideApp.with(activity)

    @Provides
    fun firebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun imageLoader(requestManager: RequestManager, firebaseStorage: FirebaseStorage): ImageLoader =
        GlideImageLoader(requestManager, firebaseStorage)
}
