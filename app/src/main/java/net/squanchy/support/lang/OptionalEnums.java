package net.squanchy.support.lang;

import android.text.TextUtils;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;

public final class OptionalEnums {

    public static Optional<ExperienceLevel> from(String rawLevel) {
        if (TextUtils.isEmpty(rawLevel)) {
            return Optional.absent();
        } else {
            return Optional.of(ExperienceLevel.fromRawLevel(rawLevel));
        }
    }
}
