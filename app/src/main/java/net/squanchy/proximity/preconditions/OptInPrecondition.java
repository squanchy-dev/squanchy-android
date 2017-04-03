package net.squanchy.proximity.preconditions;

import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;

public class OptInPrecondition implements Precondition {

    private final ProximityOptInPersister preferences_persister;

    OptInPrecondition(ProximityOptInPersister preferences_persister) {
        this.preferences_persister = preferences_persister;
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
        return preferences_persister.userOptedIn();
    }

    @Override
    public Completable satisfy() {
        preferences_persister.storeUserOptedIn();
        return Completable.complete();
    }

    @Override
    public Optional<Integer> requestCode() {
        return Optional.absent();
    }
}
