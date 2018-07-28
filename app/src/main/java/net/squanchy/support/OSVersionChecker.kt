@file:JvmName("VersionChecker")

package net.squanchy.support

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.VisibleForTesting

val android: OSVersionChecker
    @SuppressLint("VisibleForTests") // Accessing it as private would be correct here, probably a Lint bug with Kotlin visibilities
    @JvmName("android")
    get() = AndroidOSVersionChecker()

interface OSVersionChecker {

    val sdkVersion: Int

    val isAtLeastOreo: Boolean

    val isAtLeastNougatMR1: Boolean

    val isAtLeastNougat: Boolean

    val isAtLeastMarshmallow: Boolean

    val isAtLeastLollipop: Boolean

    val isAtLeastLollipopMR1: Boolean

    val isAtLeastKitKat: Boolean

    val isAtLeastJellyBeanMR2: Boolean

    val isAtLeastJellyBeanMR1: Boolean
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal

class AndroidOSVersionChecker(sdkVersionInt: Int = Build.VERSION.SDK_INT) : OSVersionChecker {

    override val sdkVersion = sdkVersionInt

    override val isAtLeastOreo = sdkVersionInt >= Build.VERSION_CODES.O

    override val isAtLeastNougatMR1 = sdkVersionInt >= Build.VERSION_CODES.N_MR1

    override val isAtLeastNougat = sdkVersionInt >= Build.VERSION_CODES.N

    override val isAtLeastMarshmallow = sdkVersionInt >= Build.VERSION_CODES.M

    override val isAtLeastLollipopMR1 = sdkVersionInt >= Build.VERSION_CODES.LOLLIPOP_MR1

    override val isAtLeastLollipop = sdkVersionInt >= Build.VERSION_CODES.LOLLIPOP

    override val isAtLeastKitKat = sdkVersionInt >= Build.VERSION_CODES.KITKAT

    override val isAtLeastJellyBeanMR2 = sdkVersionInt >= Build.VERSION_CODES.JELLY_BEAN_MR2

    override val isAtLeastJellyBeanMR1 = sdkVersionInt >= Build.VERSION_CODES.JELLY_BEAN_MR1
}
