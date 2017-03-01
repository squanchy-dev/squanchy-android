package net.squanchy.eventdetails.domain.view;

import android.support.annotation.IntRange;
import android.support.annotation.StringRes;

import net.squanchy.R;

public enum ExperienceLevel {
    BEGINNER("beginner", 0, R.string.experience_level_beginner),
    INTERMEDIATE("intermediate", 1, R.string.experience_level_intermediate),
    ADVANCED("advanced", 2,  R.string.experience_level_advanced);

    private final String rawLevel;
    private final int imageLevel;
    private final int labelStringResId;

    ExperienceLevel(String rawLevel,
                    @IntRange(from = 0, to = 2) int imageLevel,
                    @StringRes int labelStringResId) {

        this.rawLevel = rawLevel;
        this.imageLevel = imageLevel;
        this.labelStringResId = labelStringResId;
    }

    public String rawLevel() {
        return rawLevel;
    }

    @IntRange(from = 0, to = 2)
    public int getImageLevel() {
        return imageLevel;
    }

    @StringRes
    public int labelStringResId() {
        return labelStringResId;
    }
}
