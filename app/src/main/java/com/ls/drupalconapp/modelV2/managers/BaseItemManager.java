package com.ls.drupalconapp.modelV2.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.ResponseData;
import com.ls.util.ObserverHolder;

import android.os.AsyncTask;

import java.net.HttpURLConnection;

/**
 * Created on 22.05.2015.
 */
public abstract class BaseItemManager<ClassToManage, FetchRequestToManage extends AbstractBaseDrupalEntity,ParametersClass,TagClass> {

    private DrupalClient client;
    private ObserverHolder<OnDataFetchCompleteListener<ClassToManage,TagClass>> listeners;

    protected abstract FetchRequestToManage getEntityToFetch(DrupalClient client, ParametersClass requestParams);

    protected abstract TagClass getEntityRequestTag(ParametersClass params);

    protected abstract ClassToManage readResponseFromRequest(FetchRequestToManage request, TagClass tag);

    protected abstract boolean storeResponse(ClassToManage response, TagClass tag);

    protected abstract ClassToManage restoreResponse(TagClass tag);

    private AbstractBaseDrupalEntity.OnEntityRequestListener updateResponceListener = new AbstractBaseDrupalEntity.OnEntityRequestListener() {
        @Override
        public void onRequestCompleted(AbstractBaseDrupalEntity entity, Object tag, ResponseData data) {
            applyDataUpdateComplete((FetchRequestToManage) entity, (TagClass) tag, data);
        }

        @Override
        public void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, ResponseData data) {
            applyDataUpdateFailed((FetchRequestToManage) entity,(TagClass) tag, data);
        }

        @Override
        public void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag) {

        }
    };

    protected BaseItemManager(DrupalClient client) {
        this.client = client;
        this.listeners = new ObserverHolder<>();
    }

    public TagClass fetchData(ParametersClass requestParams) {
        FetchRequestToManage request = getEntityToFetch(this.client, requestParams);
        TagClass tag = getEntityRequestTag(requestParams);
        request.pullFromServer(false, tag, updateResponceListener);
        return tag;
    }

    public void addDataFetchCompleteListener(OnDataFetchCompleteListener<ClassToManage,TagClass> listener) {
        this.listeners.registerObserver(listener);
    }

    public void removeDataFetchCompleteListener(OnDataFetchCompleteListener<ClassToManage,TagClass> listener) {
        this.listeners.unregisterObserver(listener);
    }


    protected void applyDataUpdateComplete(final FetchRequestToManage entity, final TagClass tag, final ResponseData data) {
        if (data.getStatusCode() == HttpURLConnection.HTTP_OK) {
            final ClassToManage response = readResponseFromRequest(entity, tag);
            if (response != null) {
                notifyListeners(response, data, tag, true);
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        storeResponse(response, tag);
                        return null;
                    }
                }.execute();
                return;
            }
        }
        restoreData(data, tag, data.getStatusCode() == HttpURLConnection.HTTP_NOT_MODIFIED);
    }

    protected void applyDataUpdateFailed(FetchRequestToManage entity, TagClass tag, ResponseData data) {
        restoreData(data, tag,false);
    }

    private void restoreData(final ResponseData data, final TagClass tag,final boolean successful) {
        new AsyncTask<Void,Void,ClassToManage>()
        {

            @Override
            protected ClassToManage doInBackground(Void... params) {
                return restoreResponse(tag);
            }

            @Override
            protected void onPostExecute(ClassToManage classToManage) {
                notifyListeners(classToManage, data, tag, successful);
            }
        }.execute();
    }

    private void notifyListeners(final ClassToManage result, final ResponseData data, final TagClass tag, final boolean success) {

        listeners.notifyAllObservers(new ObserverHolder.ObserverNotifier<OnDataFetchCompleteListener<ClassToManage,TagClass>>() {
            @Override
            public void onNotify(OnDataFetchCompleteListener observer) {
                if (success) {
                    observer.onDataFetchComplete(result, data, tag);
                } else {
                    observer.onDataFetchFailed(result, data, tag);
                }
            }
        });
    }

    public interface OnDataFetchCompleteListener<ResultClass,TagClass> {

        void onDataFetchComplete(ResultClass result, ResponseData data, TagClass requestTag);

        void onDataFetchFailed(ResultClass result, ResponseData data, TagClass requestTag);
    }

}
