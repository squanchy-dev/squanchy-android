package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebaseLocation {

    public Long locationId;

    public String locationName;

    public String address;

    public String number;

    public String latitude;

    public String longitude;

    public Long order;

    public Boolean deleted;

    public static class Holder {

        public List<FirebaseLocation> locations;
    }
}
