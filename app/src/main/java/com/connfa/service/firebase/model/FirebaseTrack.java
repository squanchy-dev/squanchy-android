package com.connfa.service.firebase.model;

import java.util.List;

public class FirebaseTrack {

    public Long trackId;

    public String trackName;

    public Long order;

    public Boolean deleted;

    public static class Holder {

        public List<FirebaseTrack> tracks;

    }
}
