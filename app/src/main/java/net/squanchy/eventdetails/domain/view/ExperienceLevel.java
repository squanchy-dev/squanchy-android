package net.squanchy.eventdetails.domain.view;

import android.support.annotation.IntRange;
import android.support.annotation.StringRes;

import net.squanchy.R;

public enum ExperienceLevel {
    BEGINNER(0, R.string.experience_level_beginner),
    INTERMEDIATE(1, R.string.experience_level_intermediate),
    ADVANCED(2, R.string.experience_level_advanced);

    private final int rawLevel;
    private final int labelStringResId;

    ExperienceLevel(int rawLevel, @StringRes int labelStringResId) {
        this.rawLevel = rawLevel;
        this.labelStringResId = labelStringResId;
    }

    @IntRange(from = 0, to = 2)
    public int rawLevel() {
        return rawLevel;
    }

    @StringRes
    public int labelStringResId() {
        return labelStringResId;
    }
}
