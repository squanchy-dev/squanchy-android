package net.squanchy.imageloader;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;

public final class ImageLoaderInjector {

    private ImageLoaderInjector() {
        // no instances
    }

    public static ImageLoaderComponent obtain(Activity activity) {
        return DaggerImageLoaderComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .imageLoaderModule(new ImageLoaderModule())
                .build();
    }
}
