package com.connfa.model.requests;

import android.content.Context;

import com.connfa.model.data.FloorPlan;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.BaseRequest;

import java.util.Map;

public class FloorPlansRequest extends BaseSafeConsumeContainerRequest<FloorPlan.Holder> {

    public FloorPlansRequest(Context context, DrupalClient client) {
        super(context, client, new FloorPlan.Holder());
    }

    @Override
    protected String getPath() {
        return "getFloorPlans";
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
