package net.squanchy.imageloader

import dagger.Component

@Component(modules = arrayOf(ImageLoaderModule::class))
interface ImageLoaderComponent {

    fun imageLoader(): ImageLoader
}
