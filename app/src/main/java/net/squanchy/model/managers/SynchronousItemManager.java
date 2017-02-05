package net.squanchy.model.managers;

import android.content.Context;

import net.squanchy.model.database.ILAPIDBFacade;
import net.squanchy.service.api.SquanchyRepository;

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

    public Observable<R> fetch(SquanchyRepository repository, ILAPIDBFacade facade) {
        return doFetch(repository)
                .flatMap(asyncStoreResponse(facade));
    }

    protected abstract Observable<R> doFetch(SquanchyRepository repository);

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
