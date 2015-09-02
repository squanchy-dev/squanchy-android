package com.ls.drupalconapp.model.requests;

import com.ls.drupal.AbstractDrupalEntityContainer;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.ResponseData;

/**
 * Created on 02.09.2015.
 */
public abstract class BaseSafeConsumeContainerRequest<T> extends AbstractDrupalEntityContainer<T> {

    public BaseSafeConsumeContainerRequest(DrupalClient client, T theData) {
        super(client, theData);
    }

    @Override
    protected void consumeObject(ResponseData entity) {
        if(entity.getData() != null) {
            super.consumeObject(entity);
        }
    }
}
