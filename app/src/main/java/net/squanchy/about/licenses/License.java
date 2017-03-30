package net.squanchy.about.licenses;

import android.support.annotation.StringRes;

import net.squanchy.R;

enum License {
    APACHE_2("Apache 2.0 License", R.string.license_notice_apache_2),
    GLIDE("BSD, part MIT and Apache 2.0 licenses", R.string.license_notice_glide),
    ECLIPSE_PUBLIC_LICENSE("Eclipse Public License 1.0", R.string.license_notice_eclipse_public_license),
    MIT("MIT License", R.string.license_notice_mit),
    OPEN_FONT_LICENSE("Open Font License 1.1", R.string.license_notice_open_font_license);

    private final String label;

    @StringRes
    private final int noticeResId;

    License(String label, int noticeResId) {
        this.label = label;
        this.noticeResId = noticeResId;
    }

    public String label() {
        return label;
    }

    @StringRes
    public int noticeResId() {
        return noticeResId;
    }
}
