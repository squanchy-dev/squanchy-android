package net.squanchy.proximity.preconditions;

class ImpossibleToSatisfyException extends RuntimeException {

    public ImpossibleToSatisfyException(Precondition precondition) {
        super("Unable to satisfy precondition: " + precondition.toString());
    }

    public ImpossibleToSatisfyException(Precondition precondition, Throwable cause) {
        super("Unable to satisfy precondition: " + precondition.toString(), cause);
    }
}
