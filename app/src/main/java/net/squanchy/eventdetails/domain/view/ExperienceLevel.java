package net.squanchy.eventdetails.domain.view;

import android.support.annotation.StringRes;

import net.squanchy.R;
import net.squanchy.support.lang.Optional;

public enum ExperienceLevel {
    BEGINNER("beginner", R.string.experience_level_beginner),
    INTERMEDIATE("intermediate", R.string.experience_level_intermediate),
    ADVANCED("advanced", R.string.experience_level_advanced);

    public static ExperienceLevel fromRawLevel(String rawLevel) {
        switch (rawLevel.toLowerCase()) {
            case "beginner":
                return ExperienceLevel.BEGINNER;
            case "intermediate":
                return ExperienceLevel.INTERMEDIATE;
            case "advanced":
                return ExperienceLevel.ADVANCED;
            default:
                throw new IllegalArgumentException("Invalid raw level description: " + rawLevel);
        }
    }

    private final String rawLevel;
    private final int labelStringResId;

    ExperienceLevel(String rawLevel, @StringRes int labelStringResId) {

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

    public static Optional<ExperienceLevel> fromNullableRawLevel(String rawLevel) {
        if (rawLevel == null) {
            return Optional.absent();
        } else {
            return Optional.of(ExperienceLevel.fromRawLevel(rawLevel));
        }
    }
}
