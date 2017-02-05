package net.squanchy.service.model;

import java.util.List;

public class Updates {
    private final List<Integer> ids;

    public Updates(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> ids() {
        return ids;
    }
}
