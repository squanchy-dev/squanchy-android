package net.squanchy.imageloader

import dagger.Component

@Component(modules = [ImageLoaderModule::class])
interface ImageLoaderComponent {

    fun imageLoader(): ImageLoader
}
