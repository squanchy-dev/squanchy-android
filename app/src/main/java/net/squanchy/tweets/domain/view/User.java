package net.squanchy.tweets.domain.view;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class User {

    public static User create(String name, String screenName, String photoUrl) {
        return new AutoValue_User(name, screenName, photoUrl);
    }

    public abstract String name();

    public abstract String screenName();

    public abstract String photoUrl();
}
