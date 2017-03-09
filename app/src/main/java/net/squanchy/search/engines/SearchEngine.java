package net.squanchy.search.engines;

public interface SearchEngine<T> {

    boolean matches(T element, String query);
}
