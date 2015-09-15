package com.ls.drupalcon.model.requests;

import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.model.data.InfoItem;
import com.ls.http.base.BaseRequest;

import java.util.Map;

public class InfoRequest extends BaseSafeConsumeContainerRequest<InfoItem.General> {

    public InfoRequest(DrupalClient client) {
        super(client, new InfoItem.General());
    }

    @Override
    protected String getPath() {
        return "getInfo";
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