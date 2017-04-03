package net.squanchy.proximity.preconditions;

import net.squanchy.support.lang.Optional;

import io.reactivex.Single;

interface Precondition {

    boolean NEVER_UNAVAILABLE = false;
    boolean CAN_PERFORM_SYNCHRONOUS_CHECK = true;
    boolean CANNOT_PERFORM_SYNCHRONOUS_CHECK = false;
    boolean ALWAYS_NOT_SATISFIED = false;

    boolean unavailable();

    boolean performsSynchronousSatisfiedCheck();

    boolean satisfied();

    Single<SatisfyResult> satisfy();

    Optional<Integer> requestCode();

    enum SatisfyResult {
        SUCCESS,
        WAIT_FOR_EXTERNAL_RESULT,
        ABORT
    }
}
