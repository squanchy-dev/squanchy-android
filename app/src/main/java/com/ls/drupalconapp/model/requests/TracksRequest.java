package com.ls.drupalconapp.model.requests;

import com.ls.drupal.AbstractDrupalEntityContainer;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.Track;
import com.ls.http.base.BaseRequest;

import java.util.Map;

/**
 * Created on 09.06.2015.
 */
public class TracksRequest extends AbstractDrupalEntityContainer<Track.Holder> {

    public TracksRequest(DrupalClient client) {
        super(client, new Track.Holder());
    }

    @Override
    protected String getPath() {
        return "getTracks";
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
