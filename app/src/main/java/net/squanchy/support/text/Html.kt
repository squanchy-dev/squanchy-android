package net.squanchy.support.text

import android.annotation.TargetApi
import android.os.Build
import android.text.Html
import android.text.Spanned

@TargetApi(Build.VERSION_CODES.N) // The older fromHtml() is only called pre-24
internal fun parseHtml(rawHtml: String): Spanned {
    // TODO use Dante (see https://github.com/squanchy-dev/squanchy-android/issues/322)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(rawHtml, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION") // This is a "compat" method call, we only use this on pre-N
        Html.fromHtml(rawHtml)
    }
}
