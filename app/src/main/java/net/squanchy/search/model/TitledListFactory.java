package net.squanchy.search.model;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.List;

import net.squanchy.R;
import net.squanchy.speaker.domain.view.Speaker;

public class TitledListFactory {

    @StringRes
    private static final int SPEAKER_TITLE = R.string.speaker_list_title;

    public static TitledList<Speaker> buildSpeakerList(@Nullable List<Speaker> list) {
        return new TitledList<>(SPEAKER_TITLE, list);
    }
}
