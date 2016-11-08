package com.connfa.model.requests;

import android.content.Context;

import com.connfa.model.data.Event;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.BaseRequest;

import java.util.Map;

public class SocialRequest extends BaseSafeConsumeContainerRequest<Event.Holder> {

    public SocialRequest(Context context, DrupalClient client) {
        super(context, client, new Event.Holder());
    }

    @Override
    protected String getPath() {
        return "getSocialEvents";
    }

    @Override
    protected Map<String, String> getItemRequestPostParameters() {
        return null;
    }

    @Override
    protected Map<String, Object> getItemRequestGetParameters(BaseRequest.RequestMethod method) {
        return null;
    }

}
