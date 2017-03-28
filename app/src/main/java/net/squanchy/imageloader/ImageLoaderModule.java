package net.squanchy.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class})
class ImageLoaderModule {

    @Provides
    RequestManager glideRequestManager(Context context) {
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
