package com.connfa.service.firebase.model;

import java.util.List;

public class FirebaseLevel {

    public Long levelId;

    public String levelName;

    public Long order;

    public Boolean deleted;

    public static class Holder {

        public List<FirebaseLevel> levels;

    }
}
