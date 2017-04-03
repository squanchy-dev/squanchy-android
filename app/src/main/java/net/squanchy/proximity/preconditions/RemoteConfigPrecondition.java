package net.squanchy.proximity.preconditions;

import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.support.debug.DebugPreferences;
import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

public class RemoteConfigPrecondition implements Precondition {

    private final RemoteConfig remoteConfig;
    private final DebugPreferences debugPreferences;

    private boolean satisfied;

    RemoteConfigPrecondition(RemoteConfig remoteConfig, DebugPreferences debugPreferences) {
        this.remoteConfig = remoteConfig;
        this.debugPreferences = debugPreferences;
    }

    @Override
    public boolean available() {
        return ALWAYS_AVAILABLE;
    }

    @Override
    public boolean performsSynchronousSatisfiedCheck() {
        return CAN_PERFORM_SYNCHRONOUS_CHECK;
    }

    @Override
    public boolean satisfied() {
        if (debugPreferences.contestTestingEnabled()) {
            return true;
        }

        return satisfied;
    }

    @Override
    public Completable satisfy() {
        return remoteConfig.proximityServicesEnabled()
                .flatMapCompletable(enabled -> {
                    satisfied = enabled;
                    return CompletableObserver::onComplete;
                });
    }

    @Override
    public Optional<Integer> requestCode() {
        return Optional.absent();
    }
}
