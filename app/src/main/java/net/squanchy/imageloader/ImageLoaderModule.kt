package net.squanchy.imageloader

import android.app.Activity
import com.bumptech.glide.RequestManager
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ActivityContextModule

@Module(includes = [ActivityContextModule::class])
internal class ImageLoaderModule {

    @Provides
    fun glideRequestManager(activity: Activity): RequestManager = GlideApp.with(activity)

    @Provides
    fun firebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun imageLoader(requestManager: RequestManager, firebaseStorage: FirebaseStorage): ImageLoader =
        GlideImageLoader(requestManager, firebaseStorage)
}
