package net.squanchy.proximity.preconditions;

import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;

public class OptInPrecondition implements Precondition {

    private final OptInPreferencePersister preferences_persister;

    public OptInPrecondition(OptInPreferencePersister preferences_persister) {
        this.preferences_persister = preferences_persister;
    }

    @Override
    public boolean available() {
        return true;
    }

    @Override
    public boolean satisfied() {
        return preferences_persister.getOptInPreferenceGranted();
    }

    @Override
    public Completable satisfy() {
        preferences_persister.setOptInPreferenceTo(true);
        return Completable.complete();
    }

    @Override
    public Optional<Integer> requestCode() {
        return null;
    }
}
