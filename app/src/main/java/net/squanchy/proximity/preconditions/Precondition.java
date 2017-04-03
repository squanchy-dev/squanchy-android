package net.squanchy.proximity.preconditions;

import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;

interface Precondition {

    boolean ALWAYS_AVAILABLE = true;
    boolean CAN_PERFORM_SYNCHRONOUS_CHECK = true;
    boolean CANNOT_PERFORM_SYNCHRONOUS_CHECK = false;
    boolean ALWAYS_ATTEMPT_SATISFYING = false;

    boolean available();

    boolean performsSynchronousSatisfiedCheck();

    boolean satisfied();

    Completable satisfy();

    Optional<Integer> requestCode();
}
