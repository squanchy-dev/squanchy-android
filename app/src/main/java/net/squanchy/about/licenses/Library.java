package net.squanchy.about.licenses;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class Library {

    public static Builder builder() {
        return new AutoValue_Library.Builder();
    }

    abstract CharSequence name();
    
    abstract CharSequence author();

    abstract License license();

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder name(CharSequence name);

        abstract Builder author(CharSequence author);

        abstract Builder license(License license);

        abstract Library build();

    }
}
