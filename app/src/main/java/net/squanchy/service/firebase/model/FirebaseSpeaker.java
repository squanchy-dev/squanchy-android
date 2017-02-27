package net.squanchy.service.firebase.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FirebaseSpeaker {

    @NonNull
    public String id;

    @NonNull
    public String name;

    @NonNull
    public String bio;

    @Nullable
    public String company_name;

    @Nullable
    public String company_url;

    @Nullable
    public String personal_url;

    @Nullable
    public String twitter_username;

    @Nullable
    public String photo_url;
}
