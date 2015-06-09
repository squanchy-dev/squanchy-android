package com.ls.drupalconapp.modelV2.requests;

import com.ls.drupal.AbstractDrupalEntityContainer;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.InfoItem;
import com.ls.http.base.BaseRequest;

import java.util.Map;

public class InfoRequest extends AbstractDrupalEntityContainer<InfoItem.General> {

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
    protected Map<String, String> getItemRequestGetParameters(BaseRequest.RequestMethod method) {
        return null;
    }
}