package com.ls.drupalcon.model.requests;

import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.model.data.Level;
import com.ls.http.base.BaseRequest;

import java.util.Map;

public class LevelsRequest extends BaseSafeConsumeContainerRequest<Level.Holder> {

    public LevelsRequest(DrupalClient client) {
        super(client, new Level.Holder());
    }

    @Override
    protected String getPath() {
        return "getLevels";
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
