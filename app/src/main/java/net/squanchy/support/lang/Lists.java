package net.squanchy.support.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {

    private Lists() {
        // Not instantiable
    }

    public static <T, R> List<R> map(List<T> list, Func1<T, R> function) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        List<R> result = new ArrayList<>(list.size());
        for (T t : list) {
            result.add(function.call(t));
        }

        return result;
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(list.size());
        for (T t : list) {
            if (predicate.call(t)) {
                result.add(t);
            }
        }

        return result;
    }

    public static <T> Optional<T> find(List<T> list, Func1<T, Boolean> predicate) {
        for (T t : list) {
            if (predicate.call(t)) {
                return Optional.of(t);
            }
        }

        return Optional.absent();
    }

    public static <T> boolean any(List<T> list, Predicate<T> predicate) {
        for (T t : list) {
            if (predicate.call(t)) {
                return true;
            }
        }

        return false;
    }

    public static <T> boolean all(List<T> list, Predicate<T> predicate) {
        for (T t : list) {
            if (!predicate.call(t)) {
                return false;
            }
        }

        return true;
    }
}
