package net.squanchy.schedule.domain.view;

import net.squanchy.support.lang.Optional;

final class PlaceFixtures {

    private String id = "banana-room";
    private String name = "The banana room™";
    private Optional<String> floor = Optional.of("Banana floor");

    static PlaceFixtures aPlace() {
        return new PlaceFixtures();
    }

    private PlaceFixtures() {
        // Not instantiable
    }

    public PlaceFixtures withId(String id) {
        this.id = id;
        return this;
    }

    public PlaceFixtures withName(String name) {
        this.name = name;
        return this;
    }

    public PlaceFixtures withFloor(Optional<String> floor) {
        this.floor = floor;
        return this;
    }

    Place build() {
        return new Place(id, name, floor);
    }
}
