package net.squanchy.imageloader

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.ActivityContextModule

fun imageLoaderComponent(activity: AppCompatActivity): ImageLoaderComponent =
    DaggerImageLoaderComponent.builder()
        .activityContextModule(ActivityContextModule(activity))
        .imageLoaderModule(ImageLoaderModule())
        .build()

@Component(modules = [ImageLoaderModule::class])
interface ImageLoaderComponent {

    fun imageLoader(): ImageLoader
}
