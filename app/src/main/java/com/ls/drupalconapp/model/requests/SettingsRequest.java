package com.ls.drupalconapp.model.requests;

import com.ls.drupal.AbstractDrupalEntityContainer;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.SettingsHolder;
import com.ls.http.base.BaseRequest;
import com.ls.http.base.ResponseData;

import java.util.Map;

public class SettingsRequest extends AbstractDrupalEntityContainer<SettingsHolder> {

    public SettingsRequest(DrupalClient client) {
        super(client, new SettingsHolder());
    }

    @Override
    protected String getPath() {
        return "getSettings";
    }

    @Override
    protected Map<String, String> getItemRequestPostParameters() {
        return null;
    }

    @Override
    protected Map<String, Object> getItemRequestGetParameters(BaseRequest.RequestMethod method) {
        return null;
    }

    @Override
    protected void consumeObject(ResponseData entity) {
        if(entity != null) {
            super.consumeObject(entity);
        }
    }
}
