package com.ls.drupalcon.model.requests;

import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.model.data.Type;
import com.ls.http.base.BaseRequest;

import java.util.Map;

public class TypesRequest extends BaseSafeConsumeContainerRequest<Type.Holder> {

    public TypesRequest(DrupalClient client) {
        super(client, new Type.Holder());
    }

    @Override
    protected String getPath() {
        return "getTypes";
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
