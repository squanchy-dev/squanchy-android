package com.ls.drupalcon.model.requests;

import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.model.data.Location;
import com.ls.http.base.BaseRequest;

import java.util.Map;

public class LocationRequest extends BaseSafeConsumeContainerRequest<Location.Holder> {

    public LocationRequest(DrupalClient client) {
        super(client, new Location.Holder());
    }

    @Override
    protected String getPath() {
        return "getLocations";
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
