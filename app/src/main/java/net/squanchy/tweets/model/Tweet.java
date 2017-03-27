package net.squanchy.tweets.model;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

public class Tweet {

    public static final long INVALID_ID = -1L;

    @SerializedName("created_at")
    public final String createdAt;
    @SerializedName("entities")
    public final TweetEntities entities;
    @SerializedName("id")
    public final long id;
    @SerializedName("id_str")
    public final String idStr;
    @SerializedName(
            value = "text",
            alternate = {"full_text"}
    )
    public final String text;
    @SerializedName("user")
    public final User user;

    public Tweet(String createdAt, TweetEntities entities, long id, String idStr, String text, User user) {
        this.createdAt = createdAt;
        this.entities = entities;
        this.id = id;
        this.idStr = idStr;
        this.text = text;
        this.user = user;
    }
}

