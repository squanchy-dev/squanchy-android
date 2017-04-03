package net.squanchy.proximity.preconditions;

import net.squanchy.support.lang.Optional;

import io.reactivex.Single;

public class OptInPrecondition implements Precondition {

    private final ProximityOptInPersister preferences;

    OptInPrecondition(ProximityOptInPersister preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean unavailable() {
        return NEVER_UNAVAILABLE;
    }

    @Override
    public boolean performsSynchronousSatisfiedCheck() {
        return CAN_PERFORM_SYNCHRONOUS_CHECK;
    }

    @Override
    public boolean satisfied() {
        return preferences.userOptedIn();
    }

    @Override
    public Single<SatisfyResult> satisfy() {
        // This cannot be satisfied in the satisfaction loop 😏 -- instead, it must be enabled
        // explicitly by the user. This is the Gandalf of the preconditions.
        return Single.just(SatisfyResult.ABORT);
    }

    @Override
    public Optional<Integer> requestCode() {
        return Optional.absent();
    }
}
