package com.ls.drupalconapp.modelV2.requests;

import com.ls.drupal.AbstractDrupalEntityContainer;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.Speaker;
import com.ls.drupalconapp.model.data.Type;
import com.ls.http.base.BaseRequest;

import java.util.Map;

/**
 * Created on 08.06.2015.
 */
public class SpeakersRequest extends AbstractDrupalEntityContainer<Speaker.Holder> {

    public SpeakersRequest(DrupalClient client) {
        super(client, new Speaker.Holder());
    }

    @Override
    protected String getPath() {
        return "speakers";
    }

    @Override
    protected Map<String, String> getItemRequestPostParameters() {
        return null;
    }

    @Override
    protected Map<String, String> getItemRequestGetParameters(BaseRequest.RequestMethod method) {
        return null;
    }
}
