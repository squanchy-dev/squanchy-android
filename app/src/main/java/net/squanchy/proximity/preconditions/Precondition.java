package net.squanchy.proximity.preconditions;

import net.squanchy.support.lang.Optional;

import io.reactivex.Single;

interface Precondition {

    boolean ALWAYS_AVAILABLE = true;
    boolean CAN_PERFORM_SYNCHRONOUS_CHECK = true;
    boolean CANNOT_PERFORM_SYNCHRONOUS_CHECK = false;
    boolean ALWAYS_ATTEMPT_SATISFYING = false;

    boolean available();

    boolean performsSynchronousSatisfiedCheck();

    boolean satisfied();

    Single<SatisfyResult> satisfy();

    Optional<Integer> requestCode();

    enum SatisfyResult {
        RETRY,
        ABORT
    }
}
