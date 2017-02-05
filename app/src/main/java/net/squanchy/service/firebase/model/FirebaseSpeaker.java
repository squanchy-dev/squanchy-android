package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebaseSpeaker {

    public Long speakerId;

    public String firstName;

    public String lastName;

    public String avatarImageURL;

    public String organizationName;

    public String jobTitle;

    public String characteristic;

    public String twitterName;

    public String webSite;

    public Long order;

    public Boolean deleted;

    public String email;

    public static class Holder {

        public List<FirebaseSpeaker> speakers;

    }
}
