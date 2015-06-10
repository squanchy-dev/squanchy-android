package com.ls.drupalconapp.modelV2.requests;

import com.ls.drupal.AbstractDrupalEntityContainer;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.Level;
import com.ls.http.base.BaseRequest;

import java.util.Map;

/**
 * Created on 09.06.2015.
 */
public class LevelsRequest extends AbstractDrupalEntityContainer<Level.Holder> {

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
    protected Map<String, String> getItemRequestGetParameters(BaseRequest.RequestMethod method) {
        return null;
    }
}
