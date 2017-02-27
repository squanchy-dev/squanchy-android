package net.squanchy.service.firebase.model;

import android.support.annotation.NonNull;

public class FirebaseVenue {

    @NonNull
    public String name;

    @NonNull
    public String address;

    public double lat;

    public double lon;

    @NonNull
    public String description;
}
