package net.squanchy.about.licenses

import androidx.annotation.StringRes

import net.squanchy.R

internal enum class License(
    val label: String,
    @get:StringRes val noticeResId: Int
) {
    APACHE_2("Apache 2.0 License", R.string.license_notice_apache_2),
    ECLIPSE_PUBLIC_LICENSE("Eclipse Public License 1.0", R.string.license_notice_eclipse_public_license),
    GLIDE("BSD, part MIT and Apache 2.0 licenses", R.string.license_notice_glide),
    GNU_LGPL_2_1("GNU LGPL 2.1", R.string.license_notice_lgpl_2_1),
    GNU_LGPL_3("GNU LGPL 3.0", R.string.license_notice_lgpl_3),
    OPEN_FONT_LICENSE("Open Font License 1.1", R.string.license_notice_open_font_license),
    MIT("MIT License", R.string.license_notice_mit)
}
