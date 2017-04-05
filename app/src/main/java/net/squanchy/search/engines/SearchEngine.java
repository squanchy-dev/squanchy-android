package net.squanchy.search.engines;

interface SearchEngine<T> {

    boolean matches(T element, String query);
}
