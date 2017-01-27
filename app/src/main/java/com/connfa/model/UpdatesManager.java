package com.connfa.model;

import com.connfa.model.database.ILAPIDBFacade;
import com.connfa.model.managers.SynchronousItemManager;
import com.connfa.service.ConnfaRepository;
import com.connfa.service.model.Updates;
import com.connfa.ui.drawer.DrawerManager;
import com.ls.util.ObserverHolder;

import java.io.Closeable;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class UpdatesManager implements Closeable {

    public static final int SETTINGS_REQUEST_ID = 0;
    public static final int TYPES_REQUEST_ID = 1;
    public static final int LEVELS_REQUEST_ID = 2;
    public static final int TRACKS_REQUEST_ID = 3;
    public static final int SPEAKERS_REQUEST_ID = 4;
    public static final int LOCATIONS_REQUEST_ID = 5;
    public static final int FLOOR_PLANS_REQUEST_ID = 6;
    public static final int PROGRAMS_REQUEST_ID = 7;
    public static final int BOFS_REQUEST_ID = 8;
    public static final int SOCIALS_REQUEST_ID = 9;
    public static final int POIS_REQUEST_ID = 10;
    public static final int INFO_REQUEST_ID = 11;

    private ObserverHolder<DataUpdatedListener> updateListeners;
    private CompositeDisposable disposables = new CompositeDisposable();
    private final ConnfaRepository repository;

    public static int convertEventIdToEventModePos(int eventModePos) {
        switch (eventModePos) {
            case PROGRAMS_REQUEST_ID:
                return DrawerManager.EventMode.PROGRAM.ordinal();
            case BOFS_REQUEST_ID:
                return DrawerManager.EventMode.BOFS.ordinal();
            case SOCIALS_REQUEST_ID:
                return DrawerManager.EventMode.SOCIAL.ordinal();
        }
        return 0;
    }

    public UpdatesManager(ConnfaRepository repository) {
        this.repository = repository;
        updateListeners = new ObserverHolder<>();
    }

    public void startLoading(@NotNull final UpdateCallback callback) {
        final ILAPIDBFacade facade = Model.getInstance().getFacade();

        Disposable disposable = repository.updates()
                .flatMap(updateData(repository, facade, Model.getInstance()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifyObservers(callback), notifyError(callback));
        disposables.add(disposable);
    }

    private Function<Updates, ObservableSource<List<Integer>>> updateData(final ConnfaRepository repository, final ILAPIDBFacade facade, final Model model) {
        return new Function<Updates, ObservableSource<List<Integer>>>() {
            @Override
            public ObservableSource<List<Integer>> apply(final Updates updates) throws Exception {
                return Observable.fromIterable(updates.ids())
                        .doOnSubscribe(open(facade))
                        .flatMap(fetchData(repository, facade, model))
                        .doOnTerminate(close(facade))
                        .map(new Function<Integer, List<Integer>>() {
                            @Override
                            public List<Integer> apply(Integer integer) throws Exception {
                                return updates.ids();
                            }
                        });
            }
        };
    }

    private Function<Integer, ObservableSource<Integer>> fetchData(final ConnfaRepository repository, final ILAPIDBFacade facade, final Model model) {
        return new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(final Integer id) throws Exception {
                return Observable.just(id)
                        .map(managerById(model))
                        .flatMap(fetchManagerData(id, repository, facade));
            }
        };
    }

    private Function<SynchronousItemManager, ObservableSource<Integer>> fetchManagerData(final Integer id, final ConnfaRepository repository, final ILAPIDBFacade facade) {
        return new Function<SynchronousItemManager, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(SynchronousItemManager manager) throws Exception {
                //noinspection unchecked
                return manager.fetch(repository, facade)
                        .map(new Function() {
                            @Override
                            public Integer apply(Object o) throws Exception {
                                return id;
                            }
                        });
            }
        };
    }

    private Function<? super Integer, SynchronousItemManager> managerById(final Model instance) {
        return new Function<Integer, SynchronousItemManager>() {
            @Override
            public SynchronousItemManager apply(Integer id) throws Exception {
                switch (id) {
                    case SETTINGS_REQUEST_ID:
                        return instance.getSettingsManager();
                    case TYPES_REQUEST_ID:
                        return instance.getTypesManager();
                    case LEVELS_REQUEST_ID:
                        return instance.getLevelsManager();
                    case TRACKS_REQUEST_ID:
                        return instance.getTracksManager();
                    case SPEAKERS_REQUEST_ID:
                        return instance.getSpeakerManager();
                    case LOCATIONS_REQUEST_ID:
                        return instance.getLocationManager();
                    case PROGRAMS_REQUEST_ID:
                        return instance.getProgramManager();
                    case BOFS_REQUEST_ID:
                        return instance.getBofsManager();
                    case SOCIALS_REQUEST_ID:
                        return instance.getSocialManager();
                    case POIS_REQUEST_ID:
                        return instance.getPoisManager();
                    case INFO_REQUEST_ID:
                        return instance.getInfoManager();
                    case FLOOR_PLANS_REQUEST_ID:
                        return instance.getFloorPlansManager();
                    default:
                        throw new IllegalArgumentException("Id not recognized: " + id);
                }
            }
        };
    }

    private Consumer<Disposable> open(final ILAPIDBFacade facade) {
        return new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                facade.open();
            }
        };
    }

    private Action close(final ILAPIDBFacade facade) {
        return new Action() {
            @Override
            public void run() throws Exception {
                facade.close();
            }
        };
    }

    private Consumer<List<Integer>> notifyObservers(final UpdateCallback callback) {
        return new Consumer<List<Integer>>() {
            @Override
            public void accept(final List<Integer> ids) throws Exception {
                updateListeners.notifyAllObservers(new ObserverHolder.ObserverNotifier<DataUpdatedListener>() {
                    @Override
                    public void onNotify(DataUpdatedListener observer) {
                        observer.onDataUpdated(ids);
                    }
                });
                callback.onDownloadSuccess();
            }
        };
    }

    private Consumer<Throwable> notifyError(final UpdateCallback callback) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Timber.e(throwable);
                callback.onDownloadError();
            }
        };
    }

    public void registerUpdateListener(DataUpdatedListener listener) {
        this.updateListeners.registerObserver(listener);
    }

    public void unregisterUpdateListener(DataUpdatedListener listener) {
        this.updateListeners.unregisterObserver(listener);
    }

    @Override
    public void close() {
        disposables.clear();
    }

    public interface DataUpdatedListener {

        void onDataUpdated(List<Integer> requestIds);
    }

    public void checkForDatabaseUpdate() {
        ILAPIDBFacade facade = Model.getInstance().getFacade();
        facade.open();
        facade.close();
    }

}
