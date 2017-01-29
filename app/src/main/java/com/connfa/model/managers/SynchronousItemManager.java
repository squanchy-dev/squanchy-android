package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.database.ILAPIDBFacade;
import com.connfa.service.ConnfaRepository;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public abstract class SynchronousItemManager<R, T> {

    private final Context context;

    public SynchronousItemManager(Context context) {
        this.context = context;
    }

    protected abstract T getEntityRequestTag();

    protected abstract boolean storeResponse(R requestResponse, T tag);

    public Observable<R> fetch(ConnfaRepository repository, ILAPIDBFacade facade) {
        return doFetch(repository)
                .flatMap(asyncStoreResponse(facade));
    }

    protected abstract Observable<R> doFetch(ConnfaRepository repository);

    private Function<R, ObservableSource<R>> asyncStoreResponse(final ILAPIDBFacade facade) {
        return r -> Observable.fromCallable(() -> {
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
        });
    }

    public Context getContext() {
        return context;
    }
}
