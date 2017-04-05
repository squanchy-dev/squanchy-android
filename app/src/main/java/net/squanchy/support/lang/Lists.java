package net.squanchy.support.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {

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

    public static <T, R> R reduce(R initial, List<T> list, Func2<R, T, R> reducer) {
        R reducedValue = initial;
        for (T t : list) {
            reducedValue = reducer.call(reducedValue, t);
        }

        return reducedValue;
    }

    public static <T> boolean any(List<T> list, Predicate<T> predicate) {
        if (list == null) {
            return false;
        }
        for (T t : list) {
            if (predicate.call(t)) {
                return true;
            }
        }

        return false;
    }
}
