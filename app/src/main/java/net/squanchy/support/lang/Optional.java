package net.squanchy.support.lang;

import android.support.annotation.Nullable;

public final class Optional<T> {

    @SuppressWarnings("unchecked")  // Type erasure has us covered here, we don't care
    private static final Optional ABSENT = new Optional(null);

    private final T data;

    @SuppressWarnings("unchecked")  // Type erasure has us covered here, we don't care
    public static <T> Optional<T> absent() {
        return ABSENT;
    }

    public static <T> Optional<T> fromNullable(T data) {
        if (data == null) {
            return absent();
        }
        return new Optional<>(data);
    }

    public static <T> Optional<T> of(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null. Use Optional.fromNullable(maybeNullData).");
        }
        return new Optional<>(data);
    }

    private Optional(T data) {
        this.data = data;
    }

    public boolean isPresent() {
        return data != null;
    }

    public T get() {
        if (!isPresent()) {
            throw new IllegalStateException("You must check if data is present before using get()");
        }
        return data;
    }

    public T or(T elseCase) {
        return isPresent() ? get() : elseCase;
    }

    public Optional<T> or(Optional<T> elseCase) {
        return isPresent() ? this : elseCase;
    }

    public Optional<T> or(Func0<Optional<T>> elseFunc) {
        return isPresent() ? this : elseFunc.call();
    }

    @Nullable
    public T orNull() {
        return isPresent() ? get() : null;
    }

    public <V> Optional<V> map(Func1<T, V> func) {
        return flatMap(element -> of(func.call(element)));
    }

    public Optional<T> filter(Predicate<T> predicate) {
        return flatMap(element -> fromNullable(predicate.call(element) ? element : null));
    }

    public <V> Optional<V> flatMap(Func1<T, Optional<V>> func) {
        return isPresent() ? func.call(data) : Optional.absent();
    }

    @SuppressWarnings("PMD.ConfusingTernary") // Generated code
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Optional<?> optional = (Optional<?>) o;

        return data != null ? data.equals(optional.data) : optional.data == null;
    }

    @SuppressWarnings("PMD.ConfusingTernary") // Generated code
    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Optional<%s>", isPresent() ? data.toString() : "Absent");
    }
}
