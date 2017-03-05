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

    public static <T> T find(List<T> list, Func1<T, Boolean> predicate) {
        for (T t : list) {
            if (predicate.call(t)) {
                return t;
            }
        }

        // todo optional?
        return null;
    }

    public static <T, R> R reduce(R initial, List<T> list, Func2<R, T, R> reducer) {
        for (T t : list) {
            initial = reducer.apply(initial, t);
        }

        return initial;
    }
}
