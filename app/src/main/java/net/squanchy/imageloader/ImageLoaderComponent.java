package net.squanchy.imageloader;

import dagger.Component;

@Component(modules = {ImageLoaderModule.class})
public interface ImageLoaderComponent {

    ImageLoader imageLoader();
}
