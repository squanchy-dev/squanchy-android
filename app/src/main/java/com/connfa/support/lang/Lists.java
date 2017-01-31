package com.connfa.support.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {

    public static <T, R> List<R> map(List<T> list, Function<T, R> function) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        List<R> result = new ArrayList<R>(list.size());
        for (T t : list) {
            result.add(function.apply(t));
        }

        return result;
    }

    public static <T> T find(List<T> list, Function<T, Boolean> predicate) {
        for (T t : list) {
            if (predicate.apply(t)) {
                return t;
            }
        }

        // todo optional?
        return null;
    }

    public static <T, R> R reduce(R initial, List<T> list, BiFunction<R, T, R> reducer) {
        for (T t : list) {
            initial = reducer.apply(initial, t);
        }

        return initial;
    }

    public interface Function<T, R> {

        R apply(T t);
    }

    public interface BiFunction<T, U, R> {

        R apply(T t, U u);
    }
}
