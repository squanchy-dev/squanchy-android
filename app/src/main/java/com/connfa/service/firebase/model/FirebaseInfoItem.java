package com.connfa.service.firebase.model;

import java.util.HashMap;
import java.util.List;

public class FirebaseInfoItem {

    public Long infoId;

    public String infoTitle;

    public String html;

    public Long order;

    public Boolean deleted;

    public static class General {

        public HashMap<String, String> title;

        public List<FirebaseInfoItem> info;

    }

}
