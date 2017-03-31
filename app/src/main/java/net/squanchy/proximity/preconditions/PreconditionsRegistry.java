package net.squanchy.proximity.preconditions;

import java.util.List;

import net.squanchy.support.lang.Lists;
import net.squanchy.support.lang.Optional;

class PreconditionsRegistry {

    private final List<Precondition> preconditions;

    PreconditionsRegistry(List<Precondition> preconditions) {
        this.preconditions = preconditions;
    }

    boolean anyUnsatisfied() {
        for (Precondition precondition : preconditions) {
            if (!precondition.satisfied()) {
                return true;
            }
        }
        return false;
    }

    boolean isEmpty() {
        return preconditions.isEmpty();
    }

    Precondition firstPrecondition() {
        return preconditions.get(0);
    }

    Optional<Precondition> findPreconditionHandlingRequestCode(int requestCode) {
        return Lists.find(preconditions, precondition -> {
            Optional<Integer> handledCode = precondition.requestCode();
            return handledCode.isPresent() && handledCode.get() == requestCode;
        });
    }

    Optional<Precondition> preconditionAfter(Precondition precondition) {
        int preconditionPosition = preconditions.indexOf(precondition);
        if (preconditionPosition < 0) {
            throw new IllegalArgumentException("The precondition " + precondition.toString() + " is not in the registry!");
        }

        return getPreconditionAfterPosition(preconditionPosition);
    }

    private Optional<Precondition> getPreconditionAfterPosition(int preconditionPosition) {
        int nextPreconditionPosition = preconditionPosition + 1;

        if (nextPreconditionPosition >= preconditions.size()) {
            return Optional.absent();
        } else {
            return Optional.of(preconditions.get(nextPreconditionPosition));
        }
    }
}
