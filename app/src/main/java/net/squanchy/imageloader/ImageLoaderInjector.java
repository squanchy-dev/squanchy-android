package net.squanchy.imageloader;

import android.support.v7.app.AppCompatActivity;

import net.squanchy.injection.ActivityContextModule;

public final class ImageLoaderInjector {

    private ImageLoaderInjector() {
        // no instances
    }

    public static ImageLoaderComponent obtain(AppCompatActivity activity) {
        return DaggerImageLoaderComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .imageLoaderModule(new ImageLoaderModule())
                .build();
    }
}
