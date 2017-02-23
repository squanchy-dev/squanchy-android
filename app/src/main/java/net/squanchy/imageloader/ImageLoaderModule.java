package net.squanchy.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import dagger.Module;
import dagger.Provides;

@Module
class ImageLoaderModule {

    private final Context context;

    ImageLoaderModule(Context context) {
        this.context = context;
    }

    @Provides
    RequestManager glideRequestManager() {
        return Glide.with(context);
    }

    @Provides
    FirebaseImageLoader firebaseImageLoader() {
        return new FirebaseImageLoader();
    }

    @Provides
    ImageLoader imageLoader(RequestManager requestManager, FirebaseImageLoader firebaseImageLoader) {
        return new GlideImageLoader(requestManager, firebaseImageLoader);
    }
}
