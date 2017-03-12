package net.squanchy.search.view;

import android.support.annotation.StringRes;

import net.squanchy.R;

public enum HeaderType {
    SPEAKERS(R.string.speaker_list_title),
    EVENTS(R.string.talks_list_title),
    TRACKS(R.string.speaker_list_title);

    @StringRes
    private final int headerTextResourceId;

    HeaderType(@StringRes int headerTextResourceId) {
        this.headerTextResourceId = headerTextResourceId;
    }

    @StringRes
    public int headerTextResourceId() {
        return headerTextResourceId;
    }
}
