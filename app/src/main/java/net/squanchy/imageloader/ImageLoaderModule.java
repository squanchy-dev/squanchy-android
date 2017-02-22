package net.squanchy.imageloader;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class ImageLoaderModule {

    private final Context context;

    ImageLoaderModule(Context context) {
        this.context = context;
    }

    @Provides
    ImageLoader imageLoader() {
        return new GlideImageLoader(context);
    }
}
