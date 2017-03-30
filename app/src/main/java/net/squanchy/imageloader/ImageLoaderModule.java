package net.squanchy.imageloader;

import android.app.Activity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class})
class ImageLoaderModule {

    @Provides
    RequestManager glideRequestManager(Activity activity) {
        return Glide.with(activity);
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
