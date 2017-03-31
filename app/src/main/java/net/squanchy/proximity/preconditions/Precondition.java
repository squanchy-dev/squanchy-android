package net.squanchy.proximity.preconditions;

import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;

interface Precondition {

    boolean ALWAYS_ATTEMPT_SATISFYING = false;

    boolean available();

    boolean satisfied();

    Completable satisfy();

    Optional<Integer> requestCode();

    enum SatisfactionResult {
        SUCCESS,
        RETRY
    }
}
