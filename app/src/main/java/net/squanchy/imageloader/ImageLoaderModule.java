package net.squanchy.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

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
    };

    @Provides
    ImageLoader imageLoader(RequestManager requestManager) {
        return new GlideImageLoader(requestManager);
    }
}
