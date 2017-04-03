package net.squanchy.proximity.preconditions;

import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.support.debug.DebugPreferences;
import net.squanchy.support.lang.Optional;

import io.reactivex.Single;

public class RemoteConfigPrecondition implements Precondition {

    private final RemoteConfig remoteConfig;
    private final DebugPreferences debugPreferences;

    RemoteConfigPrecondition(RemoteConfig remoteConfig, DebugPreferences debugPreferences) {
        this.remoteConfig = remoteConfig;
        this.debugPreferences = debugPreferences;
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
        if (debugPreferences.contestTestingEnabled()) {
            return true;
        }

        // This means we'll rely on the result of satisfy() to determine if we want to proceed
        return ALWAYS_NOT_SATISFIED;
    }

    @Override
    public Single<SatisfyResult> satisfy() {
        return remoteConfig.proximityServicesEnabled()
                .map(enabled -> enabled ? SatisfyResult.SUCCESS : SatisfyResult.ABORT);
    }

    @Override
    public Optional<Integer> requestCode() {
        return Optional.absent();
    }
}
