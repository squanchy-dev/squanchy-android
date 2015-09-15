package com.ls.drupalcon.model.requests;

import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.model.data.Event;
import com.ls.http.base.BaseRequest;

import java.util.Map;

public class BofsRequest extends BaseSafeConsumeContainerRequest<Event.Holder> {

    public BofsRequest(DrupalClient client) {
        super(client, new Event.Holder());
    }

    @Override
    protected String getPath() {
        return "getBofs";
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
