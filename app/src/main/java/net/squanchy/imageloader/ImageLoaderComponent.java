package net.squanchy.imageloader;

import net.squanchy.injection.ActivityLifecycle;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {ImageLoaderModule.class})
public interface ImageLoaderComponent {

    ImageLoader imageLoader();
}
