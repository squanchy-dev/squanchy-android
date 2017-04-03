package net.squanchy.proximity.preconditions;

import net.squanchy.support.lang.Optional;

import io.reactivex.Single;

interface Precondition {

    boolean ALWAYS_NOT_SATISFIED = false;

    boolean satisfied();

    Single<SatisfyResult> satisfy();

    Optional<Integer> requestCode();

    enum SatisfyResult {
        SUCCESS,
        WAIT_FOR_EXTERNAL_RESULT,
        ABORT
    }
}
