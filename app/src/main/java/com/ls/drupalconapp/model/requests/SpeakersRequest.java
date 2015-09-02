package com.ls.drupalconapp.model.requests;

import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.Speaker;
import com.ls.http.base.BaseRequest;

import java.util.Map;

/**
 * Created on 08.06.2015.
 */
public class SpeakersRequest extends BaseSafeConsumeContainerRequest<Speaker.Holder> {

    public SpeakersRequest(DrupalClient client) {
        super(client, new Speaker.Holder());
    }

    @Override
    protected String getPath() {
        return "getSpeakers";
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
