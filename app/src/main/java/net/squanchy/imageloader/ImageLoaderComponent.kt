package net.squanchy.imageloader

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component

internal fun imageLoaderComponent(activity: AppCompatActivity): ImageLoaderComponent =
    DaggerImageLoaderComponent.builder()
        .activity(activity)
        .build()

@Component(modules = [ImageLoaderModule::class])
internal interface ImageLoaderComponent {

    fun imageLoader(): ImageLoader

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): ImageLoaderComponent
    }
}
