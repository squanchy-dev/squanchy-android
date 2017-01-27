package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.database.ILAPIDBFacade;
import com.connfa.service.ConnfaRepository;
import com.ls.drupal.DrupalClient;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public abstract class SynchronousItemManager<R, T> {

    private final Context context;
    private final DrupalClient client;

    public SynchronousItemManager(Context context, DrupalClient client) {
        this.context = context;
        this.client = client;
    }

    protected abstract T getEntityRequestTag();

    public abstract boolean storeResponse(R requestResponse, T tag);

    public Observable<R> fetch(ConnfaRepository repository, ILAPIDBFacade facade) {
        return doFetch(repository)
                .flatMap(asyncStoreResponse(facade));
    }

    protected abstract Observable<R> doFetch(ConnfaRepository repository);

    private Function<R, ObservableSource<R>> asyncStoreResponse(final ILAPIDBFacade facade) {
        return new Function<R, ObservableSource<R>>() {
            @Override
            public ObservableSource<R> apply(final R r) throws Exception {
                return Observable.fromCallable(new Callable<R>() {
                    @Override
                    public R call() throws Exception {
                        try {
                            facade.beginTransactions();
                            boolean result = storeResponse(r, getEntityRequestTag());
                            if (result) {
                                facade.setTransactionSuccesfull();
                            }
                        } finally {
                            facade.endTransactions();
                        }
                        return r;
                    }
                });
            }
        };
    }

    public Context getContext() {
        return context;
    }

    public DrupalClient getClient() {
        return client;
    }
}
