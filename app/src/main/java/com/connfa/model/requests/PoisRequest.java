package com.connfa.model.requests;

import android.content.Context;

import com.connfa.model.data.POI;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.BaseRequest;

import java.util.Map;

public class PoisRequest extends BaseSafeConsumeContainerRequest<POI.Holder> {

    public PoisRequest(Context context, DrupalClient client) {
        super(context, client, new POI.Holder());
    }

    @Override
    protected String getPath() {
        return "getPOI";
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
