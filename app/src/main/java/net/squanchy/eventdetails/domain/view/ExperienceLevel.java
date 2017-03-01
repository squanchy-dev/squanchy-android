package net.squanchy.eventdetails.domain.view;

import android.support.annotation.StringRes;

import net.squanchy.R;

public enum ExperienceLevel {
    BEGINNER("beginner", R.string.experience_level_beginner),
    INTERMEDIATE("intermediate", R.string.experience_level_intermediate),
    ADVANCED("advanced", R.string.experience_level_advanced);

    private final String rawLevel;
    private final int labelStringResId;

    ExperienceLevel(String rawLevel,
                    @StringRes int labelStringResId) {

        this.rawLevel = rawLevel;
        this.labelStringResId = labelStringResId;
    }

    public String rawLevel() {
        return rawLevel;
    }

    @StringRes
    public int labelStringResId() {
        return labelStringResId;
    }
}
