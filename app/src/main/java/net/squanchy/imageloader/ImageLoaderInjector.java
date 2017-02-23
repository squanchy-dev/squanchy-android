package net.squanchy.imageloader;

import android.content.Context;

public final class ImageLoaderInjector {

    private ImageLoaderInjector() {
        // no instances
    }

    public static ImageLoaderComponent obtain(Context context) {
        return DaggerImageLoaderComponent.builder()
                .imageLoaderModule(new ImageLoaderModule(context))
                .build();
    }
}
